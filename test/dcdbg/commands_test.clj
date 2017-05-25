(ns dcdbg.commands-test
  (:require [clojure.test :refer :all]
            [dcdbg.commands :refer :all]
            [repl-games.random :as rand]))

(deftest move-test
  (let [game {:zones [{:name :line-up
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
      (is (= {:zones [{:name :line-up
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
      (is (= {:zones [{:name :line-up
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
      (is (= {:zones [{:name :line-up
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
  (let [game {:zones [{:name :kick
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
      (is (= {:zones [{:name :kick
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
      (is (= {:zones [{:name :kick
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
  (let [game {:zones [{:name :hand
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
      (is (= {:zones [{:name :hand
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
      (is (= {:zones [{:name :hand
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
      (is (= {:zones [{:name :hand
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
  (let [game1 {:zones [{:name :hand
                        :type :pile
                        :facing :up
                        :cards []}]}
        game2 {:zones [{:name :hand
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
    (testing "Do nothing if hand is empty."
      (is (= game1
             (discard-hand game1))))
    (testing "Discard hand."
      (is (= {:zones [{:name :hand
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
             (discard-hand game2))))))

(deftest exec-super-villain-plan-test
  (let [game {:zones [{:name :destroyed
                       :type :pile
                       :facing :down
                       :cards [{:id 0
                                :name "A"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:id 1
                                :name "B"
                                :facing :up}
                               {:id 2
                                :name "C"
                                :facing :up}]}]
              :last-line-up-id 3}]
    (testing "Do nothing if last Line-Up card was purchased."
      (is (= game
             (exec-super-villain-plan game))))
    (testing "Destroy last Line-Up card."
      (is (= {:zones [{:name :destroyed
                       :type :pile
                       :facing :down
                       :cards [{:id 2
                                :name "C"
                                :facing :down}
                               {:id 0
                                :name "A"
                                :facing :down}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:id 1
                                :name "B"
                                :facing :up}]}]
              :last-line-up-id 2}
             (-> game
                 (assoc :last-line-up-id 2)
                 (exec-super-villain-plan)))))))

(deftest advance-timer-test
  (let [game {:zones [{:name :weakness
                       :type :stack
                       :facing :down
                       :cards [{:name "A"
                                :facing :down}
                               {:name "B"
                                :facing :down}]}
                      {:name :timer
                       :type :stack
                       :facing :up
                       :cards [{:name "C"
                                :facing :up}]}]}]
    (testing "Flip over the top Weakness card."
      (is (= {:zones [{:name :weakness
                       :type :stack
                       :facing :down
                       :cards [{:name "B"
                                :facing :down}]}
                      {:name :timer
                       :type :stack
                       :facing :up
                       :cards [{:name "A"
                                :facing :up}
                               {:name "C"
                                :facing :up}]}]}
             (advance-timer game))))))

(deftest refill-line-up-test
  (let [game {:zones [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards [{:id 0
                                :name "A"
                                :facing :down
                                :type :hero
                                :attack "attack"}
                               {:id 1
                                :name "B"
                                :facing :down
                                :type :villain
                                :attack "attack"}]}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:id 2
                                :name "C"
                                :facing :up}
                               {:id 3
                                :name "D"
                                :facing :up}
                               {:id 4
                                :name "E"
                                :facing :up}]}]}]
    (testing "Refill Line-Up from main deck."
      (is (= {:messages ["ATTACK (A): attack"
                         "ATTACK (B): attack"]
              :zones [{:name :main-deck
                       :type :stack
                       :facing :down
                       :cards []}
                      {:name :line-up
                       :type :pile
                       :facing :up
                       :cards [{:id 1
                                :name "B"
                                :facing :up
                                :type :villain
                                :attack "attack"}
                               {:id 0
                                :name "A"
                                :facing :up
                                :type :hero
                                :attack "attack"}
                               {:id 2
                                :name "C"
                                :facing :up}
                               {:id 3
                                :name "D"
                                :facing :up}
                               {:id 4
                                :name "E"
                                :facing :up}]}]
              :last-line-up-id 4}
             (refill-line-up game))))))

(deftest flip-super-villain-test
  (let [game {:zones [{:name :super-villain
                       :type :stack
                       :facing :down
                       :cards [{:name "SV"
                                :facing :up
                                :attack "attack"
                                :ongoing "ongoing"}]}]}]
    (testing "Do nothing if top Super-Villain is face-up."
      (is (= game
             (flip-super-villain game))))
    (testing "Flip over a new Super-Villain and show its effects."
      (is (= {:messages ["ATTACK (SV): attack"
                         "ONGOING (SV): ongoing"]
              :zones [{:name :super-villain
                       :type :stack
                       :facing :down
                       :cards [{:name "SV"
                                :facing :up
                                :attack "attack"
                                :ongoing "ongoing"}]}]}
             (-> game
                 (assoc-in [:zones 0 :cards 0 :facing] :down)
                 (flip-super-villain)))))))
