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

(deftest discard-hand-test
  (let [game {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "B"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards []}]}]
    (testing "Discard entire hand."
      (is (= {:state [{:name :hand
                       :type :pile
                       :facing :up
                       :cards []}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}]}
             (discard-hand game))))))

(deftest exec-super-villain-plan-test)

(deftest refill-line-up-test
  (let [game1 {:state [{:name :line-up
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
                                 :facing :up}]}]}
        game2 {:state [{:name :main-deck
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
    (testing "Do nothing if line-up has enough cards."
      (is (= game1
             (refill-line-up game1))))
    (testing "Refill line-up from main deck."
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
             (refill-line-up game2))))))

(deftest exec-villains-plan-test)

(deftest flip-super-villain-test
  (let [game {:state [{:name :super-villain
                       :type :stack
                       :facing :down
                       :cards [{:name "SV"
                                :facing :up
                                :stack-ongoing "ONGOING"
                                :first-appearance-attack "ATTACK"}]}]}]
    (testing "Do nothing if top super-villain is face-up."
      (is (= game
             (flip-super-villain game))))
    (testing "If top super-villain is face-down, flip it up and show effects."
      (is (= {:messages ["SUPER-VILLAIN ONGOING: ONGOING"
                         "SUPER-VILLAIN ATTACK: ATTACK"]
              :state [{:name :super-villain
                       :type :stack
                       :facing :down
                       :cards [{:name "SV"
                                :facing :up
                                :stack-ongoing "ONGOING"
                                :first-appearance-attack "ATTACK"}]}]}
             (-> game
                 (assoc-in [:state 0 :cards 0 :facing] :down)
                 (flip-super-villain)))))))

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
