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

(deftest refill-line-up-test
  (let [game {:state [{:name :main-deck
                       :cards [{:name "A"
                                :facing :down
                                :attack "Attack"}]}
                      {:name :line-up
                       :cards []}]}]
    (testing "Should be able to refill line-up from main deck."
      (is (= {:messages ["VILLAIN ATTACK: Attack"]
              :state [{:name :main-deck
                       :cards []}
                      {:name :line-up
                       :cards [{:name "A"
                                :facing :up
                                :attack "Attack"}]}]}
             (refill-line-up game))))))

(deftest end-turn-test
  (let [game1 {:state [{:name :super-villain
                        :cards [{:name "Crisis Anti-Monitor (Imp.)"
                                 :facing :up
                                 :stack-ongoing "Ongoing"
                                 :first-appearance-attack "Attack"}]}
                       {:name :timer
                        :cards [{:name "Weakness"
                                 :facing :down}]}
                       {:name :weakness
                        :cards []}
                       {:name :main-deck
                        :cards [{:name "Bane"
                                 :facing :down
                                 :attack "Attack"}]}
                       {:name :line-up
                        :cards []}
                       {:name :hand
                        :cards [{:name "Super Speed"
                                 :facing :up}
                                {:name "The Fastest Man Alive"
                                 :facing :up}]}
                       {:name :deck
                        :cards [{:name "Arkham Asylum"
                                 :facing :down}
                                {:name "The Batcave"
                                 :facing :down}
                                {:name "Fortress of Solitude"
                                 :facing :down}
                                {:name "Titans Tower"
                                 :facing :down}
                                {:name "The Watchtower"
                                 :facing :down}
                                {:name "Monument Point"
                                 :facing :down}]}
                       {:name :discard
                        :cards []}]}
        game2 {:messages ["VILLAIN ATTACK: Attack"]
               :state [{:name :super-villain
                        :cards [{:name "Crisis Anti-Monitor (Imp.)"
                                 :facing :up
                                 :stack-ongoing "Ongoing"
                                 :first-appearance-attack "Attack"}]}
                       {:name :timer
                        :cards []}
                       {:name :weakness
                        :cards [{:name "Weakness"
                                 :facing :down}]}
                       {:name :main-deck
                        :cards []}
                       {:name :line-up
                        :cards [{:name "Bane"
                                 :facing :up
                                 :attack "Attack"}]}
                       {:name :hand
                        :cards [{:name "Arkham Asylum"
                                 :facing :up}
                                {:name "The Batcave"
                                 :facing :up}
                                {:name "Fortress of Solitude"
                                 :facing :up}
                                {:name "Titans Tower"
                                 :facing :up}
                                {:name "The Watchtower"
                                 :facing :up}]}
                       {:name :deck
                        :cards [{:name "Monument Point"
                                 :facing :down}]}
                       {:name :discard
                        :cards [{:name "Super Speed"
                                 :facing :down}
                                {:name "The Fastest Man Alive"
                                 :facing :down}]}]}]
    (testing "Should draw 5 cards by default at end of turn."
      (is (= game2
             (end-turn game1))))
    (testing "Should be able to draw n cards at end of turn."
      (is (= (-> game2
                 (assoc-in [:state 5 :cards] [{:name "Arkham Asylum"
                                               :facing :up}
                                              {:name "The Batcave"
                                               :facing :up}
                                              {:name "Fortress of Solitude"
                                               :facing :up}
                                              {:name "Titans Tower"
                                               :facing :up}
                                              {:name "The Watchtower"
                                               :facing :up}
                                              {:name "Monument Point"
                                               :facing :up}])
                 (assoc-in [:state 6 :cards] []))
             (end-turn game1 6))))
    (testing "Should flip top super-villain if it's face-down."
      (is (= (assoc game2 :messages ["VILLAIN ATTACK: Attack"
                                     "SUPER-VILLAIN ONGOING: Ongoing"
                                     "SUPER-VILLAIN ATTACK: Attack"])
             (-> game1
                 (assoc-in [:state 0 :cards 0 :facing] :down)
                 (end-turn)))))))
