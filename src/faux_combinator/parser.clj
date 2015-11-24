(ns faux-combinator.parser)

(def ^:private ^:dynamic current-parser nil)

(defn do-parser
  "Returns a function that has a parser for the DSL-ish syntax.
   This function is public only because `make-parser` uses it."
  [parser]
  (fn [tokens]
    (dosync
     (binding [current-parser (atom {:tokens tokens})]
       (parser)))))

;; extractors
(def ^:private token-type first)
(def ^:private token-value second)

(defmacro make-parser
  "Simple wrapper around `do-parser` that thunks its rules"
  [& code]
  `(do-parser (fn [] ~@code)))

(defn expect
  [expect-type]
  (let [token (first (:tokens @current-parser))]
    (if (= expect-type (token-type token))
      (do
        (swap! current-parser rest)
        (token-value token))
      (throw (Exception. (str "expected token " expect-type ", got "
                              (token-type token)))))))

(defmacro maybe
  "Simple thunking wrapper around `maybe-impl`"
  [code]
  `(maybe-impl (fn [] ~code)))

(defn maybe-impl
  "This tries to run a piece of match, and restores tokens if it didn't match"
  [code]
  (let [prev-tokens @current-parser]
    (try
      (code)
      (catch Exception e
        ; run for the hills! reset the parser!
        (reset! current-parser prev-tokens)
        false))))

(defmacro one-of
  "Thunks every piece independently and passes it to `one-of-impl`"
  [& code]
  `(one-of-impl ~@(map #(list 'fn [] %) code)))

(defn one-of-impl
  "Tries every alternative, throws if none matches"
  [& code]
  (or
   (some maybe-impl code)
   (throw (Exception. "No matching case of `one-of`"))))


(defmacro any-of
  "Thunks its argument and calls `any-of-impl`"
  [& code]
  `(any-of-impl (fn [] ~@code)))

(defn any-of-impl
  "Tries to match `code` as many times as possible (zero if needed)"
  [code]
  (loop [results []]
    (if-let [result (maybe-impl code)]
      (recur (conj results result))
      results)))

(defmacro many-of
  "Thunks its argument and calls `many-of-impl`"
  [& code]
  `(many-of-impl (fn [] ~@code)))

(defn many-of-impl
  "Tries to match `code` one or more times"
  [code]
  (into [(code)] (any-of-impl code)))
