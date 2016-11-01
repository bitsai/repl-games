(ns dcdbg.commands-test
  (:require [clojure.test :refer :all]
            [dcdbg.commands :refer :all]))

(deftest move-test
  (let [game {:state [{:name :line-up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "D"
                                :facing :down}]}]}]
    (testing "Should be able to move 1 card."
      (is (= {:state [{:name :line-up
                       :cards [{:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "A"
                                :facing :down}
                               {:name "D"
                                :facing :down}]}]}
             (move game 0 1 :top 0))))
    (testing "Should be able to move multiple cards."
      (is (= {:state [{:name :line-up
                       :cards [{:name "B"
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "A"
                                :facing :down}
                               {:name "C"
                                :facing :down}
                               {:name "D"
                                :facing :down}]}]}
             (move game 0 1 :top 0 2))))
    (testing "Should be able to move to the bottom."
      (is (= {:state [{:name :line-up
                       :cards [{:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "D"
                                :facing :down}
                               {:name "A"
                                :facing :down}]}]}
             (move game 0 1 :bottom 0))))))
