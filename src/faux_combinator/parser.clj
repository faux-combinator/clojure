(ns faux-combinator.parser)

(def ^:dynamic current-parser nil)

(defn- get-current-tokens
  )

(defmacro make-parser
  "Installs a parser for the DSL-ish syntax"
  [& code]
  `(fn [] ~@code))
