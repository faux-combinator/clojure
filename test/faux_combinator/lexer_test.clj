(ns faux-combinator.lexer-test
    (:require [clojure.test :refer :all]
              [faux-combinator.lexer :refer [lex]]))

(deftest lexer-basic-test
  (testing "parses basic stuff"
           (let [rules {:eq "="}]
                (is (= (lex rules "=") [[:eq "="]])))))
