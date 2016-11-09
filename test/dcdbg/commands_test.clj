(ns dcdbg.commands-test
  (:require [clojure.test :refer :all]
            [dcdbg.commands :refer :all]
            [repl-games.random :as rand]))

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
    (testing "Should be able to move to the top."
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
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "D"
                                :facing :down}
                               {:name "A"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}
             (move game 0 1 :bottom 0 2))))))

(deftest gain-test
  (let [game {:state [{:name :kick
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :discard
                       :cards []}]}]
    (testing "Should be able to gain 1 card by default."
      (is (= {:state [{:name :kick
                       :cards [{:name "B"
                                :facing :up}]}
                      {:name :discard
                       :cards [{:name "A"
                                :facing :down}]}]}
             (gain game 0))))
    (testing "Should be able to gain multiple cards."
      (is (= {:state [{:name :kick
                       :cards []}
                      {:name :discard
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}]}
             (gain game 0 2))))))

(deftest refill-deck-test
  (let [game1 {:state [{:name :deck
                        :cards [{:name "A"
                                 :facing :down}]}
                       {:name :discard
                        :cards [{:name "B"
                                 :facing :down}
                                {:name "C"
                                 :facing :down}]}]}
        game2 {:state [{:name :deck
                        :cards [{:name "A"
                                 :facing :down}]}
                       {:name :discard
                        :cards []}]}]
    (testing "Should be able to refill deck from discards."
      (rand/set-seed! 420)
      (is (= {:state [{:name :deck
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}
                      {:name :discard
                       :cards []}]}
             (refill-deck game1))))
    (testing "Should do nothing if discards is empty."
      (is (= game2
             (refill-deck game2))))))

(deftest draw-test
  (let [game {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}]}
                      {:name :deck
                       :cards [{:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}]
    (testing "Should draw 1 card by default."
      (is (= {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :deck
                       :cards [{:name "C"
                                :facing :down}]}]}
             (draw game))))
    (testing "Should be able to draw multiple cards."
      (is (= {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :deck
                       :cards []}]}
             (draw game 2))))))
