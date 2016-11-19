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
  (let [game {:state [{:name :deck
                       :cards [{:name "A"
                                :facing :down}]}
                      {:name :discard
                       :cards [{:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}]
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
             (refill-deck game))))))

(deftest draw-test
  (let [game {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}]}
                      {:name :deck
                       :cards [{:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}
                      {:name :discard
                       :cards [{:name "D"
                                :facing :down}]}]}]
    (testing "Should draw 1 card by default."
      (is (= {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :deck
                       :cards [{:name "C"
                                :facing :down}]}
                      {:name :discard
                       :cards [{:name "D"
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
                       :cards []}
                      {:name :discard
                       :cards [{:name "D"
                                :facing :down}]}]}
             (draw game 2))))
    (testing "Should refill then draw if needed."
      (is (= {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}
                               {:name "D"
                                :facing :up}]}
                      {:name :deck
                       :cards []}
                      {:name :discard
                       :cards []}]}
             (draw game 3))))
    (testing "Should throw exception if there aren't enough cards."
      (is (thrown? Exception
                   (draw game 4))))))

(deftest discard-hand-test
  (let [game {:state [{:name :hand
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :cards []}]}]
    (testing "Should be able to discard whole hand."
      (is (= {:state [{:name :hand
                       :cards []}
                      {:name :discard
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}
             (discard-hand game))))))

(deftest refill-line-up-test
  (let [game {:state [{:name :main-deck
                       :cards [{:name "A"
                                :facing :down
                                :attack "Attack"}]}
                      {:name :line-up
                       :cards []}]}]
    (testing "Should be able to refill line-up w/ top main deck card."
      (is (= {:messages ["VILLAIN ATTACK: Attack"]
              :state [{:name :main-deck
                       :cards []}
                      {:name :line-up
                       :cards [{:name "A"
                                :facing :up
                                :attack "Attack"}]}]}
             (refill-line-up game))))))

(deftest flip-super-villain-test
  (let [game1 {:state [{:name :super-villain
                        :cards [{:name "A"
                                 :facing :up}]}]}
        game2 {:state [{:name :super-villain
                        :cards [{:name "A"
                                 :facing :down
                                 :stack-ongoing "Ongoing"
                                 :first-appearance-attack "Attack"}]}]}]
    (testing "Should do nothing if top super-villain is already facing up."
      (is (= game1
             (flip-super-villain game1))))
    (testing "Should flip top super-villain if it's face-down."
      (is (= {:messages ["SUPER-VILLAIN ONGOING: Ongoing"
                         "SUPER-VILLAIN ATTACK: Attack"]
              :state [{:name :super-villain
                       :cards [{:name "A"
                                :facing :up
                                :stack-ongoing "Ongoing"
                                :first-appearance-attack "Attack"}]}]}
             (flip-super-villain game2))))))

(deftest advance-timer-test
  (let [game {:state [{:name :timer
                       :cards [{:name "A"
                                :facing :down}]}
                      {:name :weakness
                       :cards []}]}]
    (testing "Should be able to move card from timer to weakness stack."
      (is (= {:state [{:name :timer
                       :cards []}
                      {:name :weakness
                       :cards [{:name "A"
                                :facing :down}]}]}
             (advance-timer game))))))
