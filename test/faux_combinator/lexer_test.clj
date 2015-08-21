(ns faux-combinator.lexer-test
    (:require [clojure.test :refer :all]
              [faux-combinator.lexer :refer [lex]]))

(deftest lexer-basic-test
  (testing "it parses empty stuff"
           (let [rules {}]
                (is (= (lex rules "") []))
                (is (= (lex rules "    ") []))))

  (testing "it parses basic stuff"
           (let [rules {:eq "="}]
                (is (= (lex rules "=") [[:eq "="]]))))

  (testing "it parses multiple tokens"
           (let [rules {:eq "="}]
                (is (= (lex rules "==") [[:eq "="] [:eq "="]]))))

  (testing "it parses multiple, space-separated tokens"
           (let [rules {:eq "="}]
                (is (= (lex rules "=  =") [[:eq "="] [:eq "="]]))))
  )

(deftest lexer-multiple-rules-test
  ; XXX should these be `def`s?
  (let [eq-token [:eq "="]
        dash-token [:dash "-"]
        under-token [:under "_"]
        rules {:eq "=" :dash "-" :under "_"}]
       (testing "it parses any rule"
                (is (= (lex rules "=") [eq-token]))
                (is (= (lex rules "-") [dash-token]))
                (is (= (lex rules "_") [under-token]))
                )

       (testing "it parses multiple rules"
                (is (= (lex rules "=-_") [eq-token dash-token under-token]))
                (is (= (lex rules "= -   _") [eq-token dash-token under-token])))
       ))

; TODO (maybe) test/implement mutator form

(deftest lexer-fails-test
  (testing "it correctly fails to parse invalid strings"
           (let [rules {}]
                (is (thrown? Exception (lex rules "anything")))))
  )
