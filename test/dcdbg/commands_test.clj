(ns dcdbg.commands-test
  (:require [clojure.test :refer :all]
            [dcdbg.commands :refer :all]
            [repl-games.random :as rand]))

(deftest move-test
  (let [game {:state [{:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}]}]}]
    (testing "By default, move nothing."
      (is (= {:state [{:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}]}]}
             (move game 0 1 :top))))
    (testing "Move to the top."
      (is (= {:state [{:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "C"
                                :facing :down}
                               {:name "D"
                                :facing :down}]}]}
             (move game 0 1 :top 0 2))))
    (testing "Move to the bottom."
      (is (= {:state [{:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}
                               {:name "A"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}
             (move game 0 1 :bottom 0 2))))))

(deftest gain-test
  (let [game {:state [{:name :kick
                       :type :stack
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards []}]}]
    (testing "By default, gain 1 card."
      (is (= {:state [{:name :kick
                       :type :stack
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}]}]}
             (gain game 0))))
    (testing "Gain multiple cards."
      (is (= {:state [{:name :kick
                       :type :stack
                       :facing :up
                       :cards []}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}]}
             (gain game 0 2))))))

(deftest refill-deck-test
  (let [game {:state [{:name :deck
                       :type :stack
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}]
    (testing "Refill deck from discard."
      (rand/set-seed! 420)
      (is (= {:state [{:name :deck
                       :type :stack
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards []}]}
             (refill-deck game))))))

(deftest draw-test
  (let [game {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}]}
                      {:name :deck
                       :type :stack
                       :facing :down
                       :cards [{:name "B"
                                :facing :down}
                               {:name "C"
                                :facing :down}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}]}]}]
    (testing "By default, draw 1 card."
      (is (= {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :deck
                       :type :stack
                       :facing :down
                       :cards [{:name "C"
                                :facing :down}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}]}]}
             (draw game))))
    (testing "Draw multiple cards."
      (is (= {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :deck
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "D"
                                :facing :down}]}]}
             (draw game 2))))
    (testing "If necessary, refill deck from discard then draw."
      (is (= {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}
                               {:name "D"
                                :facing :up}]}
                      {:name :deck
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards []}]}
             (draw game 3))))
    (testing "Throw exception if there aren't enough to refill."
      (is (thrown? Exception
                   (draw game 4))))))

(deftest discard-hand-test)

(deftest exec-super-villain-plan-test)

(deftest refill-line-up-test
  (testing "Do nothing if line-up has enough cards."
    (let [game {:state [{:name :line-up
                         :type :pile
                         :facing :up
                         :cards [{:name "A"
                                  :facing :up}
                                 {:name "B"
                                  :facing :up}
                                 {:name "C"
                                  :facing :up}
                                 {:name "D"
                                  :facing :up}
                                 {:name "E"
                                  :facing :up}]}]}]
      (is (= game
             (refill-line-up game)))))
  (testing "Refill line-up from main deck."
    (let [game {:state [{:name :main-deck
                         :type :stack
                         :facing :down
                         :cards [{:name "A"
                                  :attack "A"
                                  :facing :down}
                                 {:name "B"
                                  :attack "B"
                                  :facing :down}]}
                        {:name :line-up
                         :type :pile
                         :facing :up
                         :cards [{:name "C"
                                  :facing :up}
                                 {:name "D"
                                  :facing :up}
                                 {:name "E"
                                  :facing :up}]}]}]
      (is (= {:messages ["VILLAIN ATTACK: A"
                         "VILLAIN ATTACK: B"]
              :state [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :attack "B"
                                :facing :up}
                               {:name "A"
                                :attack "A"
                                :facing :up}
                               {:name "C"
                                :facing :up}
                               {:name "D"
                                :facing :up}
                               {:name "E"
                                :facing :up}]}]}
             (refill-line-up game))))))

(deftest exec-villains-plan-test)

(deftest flip-super-villain-test)

(deftest proc-anti-monitor-ongoing-test)

(deftest advance-countdown-test
  (let [game {:state [{:name :countdown
                       :type :stack
                       :facing :down
                       :cards [{:name "W"
                                :facing :down}]}
                      {:name :weakness
                       :type :stack
                       :facing :up
                       :cards []}]}]
    (testing "Advance timer by flipping a Weakness card over."
      (is (= {:state [{:name :countdown
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :weakness
                       :type :stack
                       :facing :up
                       :cards [{:name "W"
                                :facing :up}]}]}
             (advance-countdown game))))))

(deftest end-turn-test
  (let [game1 {:state [{:name :super-villain
                        :type :stack
                        :facing :down
                        :cards [{:name "SV"
                                 :facing :up}]}
                       {:name :countdown
                        :type :stack
                        :facing :down
                        :cards [{:name "W"
                                 :facing :down}]}
                       {:name :weakness
                        :type :stack
                        :facing :up
                        :cards []}
                       {:name :line-up
                        :type :pile
                        :facing :up
                        :cards [{:name "A"
                                 :facing :up}
                                {:name "B"
                                 :facing :up}
                                {:name "C"
                                 :facing :up}
                                {:name "D"
                                 :facing :up}
                                {:name "E"
                                 :facing :up}]}
                       {:name :hand
                        :type :pile
                        :facing :up
                        :cards []}
                       {:name :deck
                        :type :stack
                        :facing :down
                        :cards [{:name "1"
                                 :facing :down}
                                {:name "2"
                                 :facing :down}
                                {:name "3"
                                 :facing :down}
                                {:name "4"
                                 :facing :down}
                                {:name "5"
                                 :facing :down}
                                {:name "6"
                                 :facing :down}]}
                       {:name :discard
                        :type :pile
                        :facing :down
                        :cards []}]}
        game2 {:state [{:name :super-villain
                        :type :stack
                        :facing :down
                        :cards [{:name "SV"
                                 :facing :up}]}
                       {:name :countdown
                        :type :stack
                        :facing :down
                        :cards []}
                       {:name :weakness
                        :type :stack
                        :facing :up
                        :cards [{:name "W"
                                 :facing :up}]}
                       {:name :line-up
                        :type :pile
                        :facing :up
                        :cards [{:name "A"
                                 :facing :up}
                                {:name "B"
                                 :facing :up}
                                {:name "C"
                                 :facing :up}
                                {:name "D"
                                 :facing :up}
                                {:name "E"
                                 :facing :up}]}
                       {:name :hand
                        :type :pile
                        :facing :up
                        :cards [{:name "1"
                                 :facing :up}
                                {:name "2"
                                 :facing :up}
                                {:name "3"
                                 :facing :up}
                                {:name "4"
                                 :facing :up}
                                {:name "5"
                                 :facing :up}]}
                       {:name :deck
                        :type :stack
                        :facing :down
                        :cards [{:name "6"
                                 :facing :down}]}
                       {:name :discard
                        :type :pile
                        :facing :down
                        :cards []}]}]
    (testing "By default, draw 5 new cards."
      (is (= game2
             (end-turn game1))))
    (testing "Draw n new cards."
      (is (= (-> game2
                 (assoc-in [:state 4 :cards] [{:name "1"
                                               :facing :up}
                                              {:name "2"
                                               :facing :up}
                                              {:name "3"
                                               :facing :up}
                                              {:name "4"
                                               :facing :up}
                                              {:name "5"
                                               :facing :up}
                                              {:name "6"
                                               :facing :up}])
                 (assoc-in [:state 5 :cards] []))
             (end-turn game1 6))))))
