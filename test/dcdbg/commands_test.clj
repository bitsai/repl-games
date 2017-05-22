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
    (testing "By default, move top card."
      (is (= {:state [{:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "D"
                                :facing :down}]}]}
             (move game :line-up :discard :top nil))))
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
             (move game :line-up :discard :top [0 2]))))
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
             (move game :line-up :discard :bottom [0 2]))))))

(deftest refill-line-up-test
  (let [game {:state [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "C"
                                :facing :up}]}]}]
    (testing "Refill line-up from main deck."
      (is (= {:state [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards [{:name "B"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}]}
             (refill-line-up game))))))

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
    (testing "Advance countdown by flipping a Weakness card over."
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
