(ns faux-combinator.parser-test
  (:require [clojure.test :refer :all]
            [faux-combinator.parser :refer :all]))

(def eq-token [:eq "="])
(def dash-token [:dash "="])
(def under-token [:under "="])
(def lparen-token [:lparen "="])
(def rparen-token [:rparen "="])

(deftest parser-basic-test
  (testing "it can parse basic stuff"
           (let [parser (make-parser
                         (expect :eq)
                         true)]
                (is (= true (parser [eq-token])))
             )))

; TODO test for failure
