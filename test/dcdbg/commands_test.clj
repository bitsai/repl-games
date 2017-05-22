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

(deftest gain-test
  (let [game {:state [{:name :kick
                       :type :stack
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
    (testing "By default, gain 1 card."
      (is (= {:state [{:name :kick
                       :type :stack
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
             (gain game :kick))))
    (testing "Gain multiple cards."
      (is (= {:state [{:name :kick
                       :type :stack
                       :facing :up
                       :cards [{:name "C"
                                :facing :up}]}
                      {:name :discard
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}
                               {:name "D"
                                :facing :down}]}]}
             (gain game :kick 2))))))

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
                       :cards [{:name "C"
                                :facing :down}]}]}]
    (testing "Discard hand."
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
                                :facing :down}
                               {:name "C"
                                :facing :down}]}]}
             (discard-hand game))))))

(deftest exec-super-villain-plan-test
  (let [game {:state [{:name :destroyed
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}]}]
    (testing "Destroy bottom Line-Up card."
      (is (= {:state [{:name :destroyed
                       :type :pile
                       :facing :down
                       :cards [{:name "C"
                                :facing :down}
                               {:name "A"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up}]}]}
             (exec-super-villain-plan game))))))

(deftest refill-line-up-test
  (let [game {:state [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards [{:name "A"
                                :facing :down
                                :type :hero
                                :attack "A"}
                               {:name "B"
                                :facing :down
                                :type :villain
                                :attack "B"}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "C"
                                :facing :up}
                               {:name "D"
                                :facing :up}
                               {:name "E"
                                :facing :up}]}]}]
    (testing "Refill Line-Up from main deck."
      (is (= {:messages ["HERO ATTACK: A"
                         "VILLAIN ATTACK: B"]
              :state [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "B"
                                :facing :up
                                :type :villain
                                :attack "B"}
                               {:name "A"
                                :facing :up
                                :type :hero
                                :attack "A"}
                               {:name "C"
                                :facing :up}
                               {:name "D"
                                :facing :up}
                               {:name "E"
                                :facing :up}]}]}
             (refill-line-up game))))))

(deftest exec-villains-plan-test
  (let [game1 {:state [{:name :line-up
                        :type :pile
                        :facing :up
                        :cards []}]}
        game2 {:state [{:name :destroyed
                        :type :pile
                        :facing :down
                        :cards []}
                       {:name :main-deck
                        :type :stack
                        :facing :down
                        :cards [{:name "A"
                                 :facing :down}
                                {:name "B"
                                 :facing :down}
                                {:name "C"
                                 :facing :down}]}
                       {:name :line-up
                        :type :pile
                        :facing :up
                        :cards [{:name "D"
                                 :facing :up
                                 :type :villain
                                 :cost 1}
                                {:name "E"
                                 :facing :up
                                 :type :villain
                                 :cost 2}]}]}]
    (testing "Do nothing if there are no villains in Line-Up."
      (is (= game1
             (exec-villains-plan game1))))
    (testing "Destroy N main deck cards (N = max villain cost in Line-Up)."
      (is (= {:state [{:name :destroyed
                       :type :pile
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}
                      {:name :main-deck
                       :type :stack
                       :facing :down
                       :cards [{:name "C"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:name "D"
                                :facing :up
                                :type :villain
                                :cost 1}
                               {:name "E"
                                :facing :up
                                :type :villain
                                :cost 2}]}]}
             (exec-villains-plan game2))))))

(deftest flip-super-villain-test
  (let [game {:state [{:name :super-villain
                       :type :stack
                       :facing :down
                       :cards [{:name "SV"
                                :facing :up
                                :stack-ongoing "ONGOING"
                                :first-appearance-attack "ATTACK"}]}]}]
    (testing "Do nothing if top Super-Villain is face-up."
      (is (= game
             (flip-super-villain game))))
    (testing "Flip over a new Super-Villain and show its effects."
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
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}
                      {:name :weakness
                       :type :stack
                       :facing :up
                       :cards [{:name "C"
                                :facing :up}]}]}]
    (testing "Flip over the top Weakness card."
      (is (= {:state [{:name :countdown
                       :type :stack
                       :facing :down
                       :cards [{:name "B"
                                :facing :down}]}
                      {:name :weakness
                       :type :stack
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}]}
             (advance-countdown game))))))
