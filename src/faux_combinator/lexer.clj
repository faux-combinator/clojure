(ns faux-combinator.lexer
    (:require [clojure.string :refer [trim]]))

(defn- make-pattern
  "converts to a string (if needed) and prepends the `^` regexp anchor"
  [string]
  (re-pattern (str "^" string)))

(defn- find-rule
  "filters to find the first matching rule"
  [rules string]
  (first (filter #(re-find (make-pattern (second %)) string)
                 rules)))

(defn lex
  "Parses `str` according to `tokens`."
  [rules string]
  (loop [string string, tokens []]
        (if (= "" string)
            tokens
          (if (re-find #"^\s+" string)
              (recur (trim string) tokens)
              (if-let [[type reg] (find-rule rules string)]
                      (let [match (re-find (make-pattern reg) string)]
                           (recur (subs string (count match))
                                  (conj tokens [type match])))
                      (throw (Exception. (str "unable to parse code: "
                                                 (subs string 0 15)))))))))


; TODO custom exception? use RuntimeException?
; use the clojure "exceptioninfo" class? (using `ex-info` then `ex-data`)
;  => how to make that work with `(is (thrown?`? is support provided?
