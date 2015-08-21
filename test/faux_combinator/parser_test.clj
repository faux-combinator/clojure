(ns faux-combinator.parser-test
  (:require [clojure.test :refer :all]
            [faux-combinator.parser :refer :all]))

(def eq-token [:eq "="])
(def dash-token [:dash "="])
(def under-token [:under "="])
(def lparen-token [:lparen "="])
(def rparen-token [:rparen "="])

(deftest a-test
  (testing "it can parse basic stuff"
           (let [parser (make-parser
                         (expect :eq))]
                1;(is (= (parser )))
             )))
