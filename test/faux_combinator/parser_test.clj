(ns faux-combinator.parser-test
  (:require [clojure.test :refer :all]
            [faux-combinator.parser :refer :all]))

(def eq-token [:eq "="])
(def dash-token [:dash "-"])
(def under-token [:under "_"])
(def lparen-token [:lparen "("])
(def rparen-token [:rparen ")"])

(deftest parser-basic-test
  (testing "it can parse basic stuff"
    (let [parser (make-parser
                  (expect :eq)
                  true)]
      (is (= true (parser [eq-token])))))
  (testing "can `maybe` match a token"
    (let [parser (make-parser
                  (maybe (expect :eq))
                  true)]
      (is (= true (parser [eq-token])))
      (is (= true (parser [])))))
  (testing "can `one-of` several alternatives"
    (let [parser (make-parser
                  (one-of (expect :eq)
                          (expect :dash)
                          (expect :under)))]
      (is (= "=" (parser [eq-token])))
      (is (= "-" (parser [dash-token])))
      (is (= "_" (parser [under-token])))
      (is (thrown? Exception (parser [])))
      (is (thrown? Exception (parser [lparen-token])))
      ))
  )

(deftest parser-fails-test
  (testing "it correctly fails without the required component(s)"
    (let [parser (make-parser
                  (expect :anything)
                  true)]
      (is (thrown? Exception (parser [])))))
  )
