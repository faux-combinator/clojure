(ns faux-combinator.parser)

(def ^:dynamic current-parser nil)

(defn do-parser
  "Returns a function that has a parser for the DSL-ish syntax.
   This function is public only because `make-parser` uses it."
  [parser]
  (fn [tokens]
    (dosync
      (binding [current-parser (atom {:tokens tokens})]
        (parser)))))

;; extractors
(def token-type first)
(def token-value second)

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
