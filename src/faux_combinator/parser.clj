(ns faux-combinator.parser)

(def ^:private ^:dynamic current-parser nil)
(def mdr (+ 2 2))

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

(defn one-of-impl [& code]
  (or
   (some maybe-impl code)
   (throw (Exception. "No matching case of `one-of`"))))

(defmacro one-of
  [& code]
  `(one-of-impl ~@(map #(list 'fn [] %) code)))
