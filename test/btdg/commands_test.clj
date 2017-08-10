(ns btdg.commands-test
  (:require [btdg.commands :refer :all]
            [clojure.test :refer :all]
            [repl-games.random :as rand]))

(deftest roll-dice-test
  (rand/set-seed! 420)
  (let [game {:dice [{:type :base}
                     {:type :base}
                     {:type :base}
                     {:type :loudmouth}
                     {:type :coward}]
              :dice-rolls 0}]
    (testing "Should roll all dice by default."
      (is (= {:dice [{:type :base :value "DYNAMITE" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :loudmouth :value "2 (2)" :new? true}
                     {:type :coward :value "BEER (2)" :new? true}]
              :dice-rolls 1}
             (roll-dice game))))
    (testing "Should be able to roll specific dice."
      (is (= {:dice [{:type :base :value "1" :new? true}
                     {:type :base}
                     {:type :base :value "1" :new? true}
                     {:type :loudmouth}
                     {:type :coward :value "BEER" :new? true}]
              :dice-rolls 1}
             (roll-dice game 0 2 4))))))

(deftest take-arrows-test
  (let [game {:players [{:arrows 0}
                        {:arrows 0}]
              :arrows 9}]
    (testing "Should take 1 arrow by default."
      (is (= {:players [{:arrows 1}
                        {:arrows 0}]
              :arrows 8}
             (take-arrows game 0))))
    (testing "Should be able to take arrows for multiple players."
      (is (= {:players [{:arrows 1}
                        {:arrows 2}]
              :arrows 6}
             (take-arrows game 0 1 1 2))))
    (testing "Should not be able to take more than arrows remaining."
      (is (= {:players [{:arrows 1}
                        {:arrows 8}]
              :arrows 0}
             (take-arrows game 0 1 1 20))))))

(deftest discard-arrows-test
  (let [game {:players [{:arrows 1}
                        {:arrows 8}]
              :arrows 0}]
    (testing "Should discard all player arrows by default."
      (is (= {:players [{:arrows 1}
                        {:arrows 0}]
              :arrows 8}
             (discard-arrows game 1))))
    (testing "Should be able to discard arrows for multiple players."
      (is (= {:players [{:arrows 0}
                        {:arrows 6}]
              :arrows 3}
             (discard-arrows game 0 1 1 2))))
    (testing "Should not be able to discard more than arrows taken."
      (is (= {:players [{:arrows 0}
                        {:arrows 0}]
              :arrows 9}
             (discard-arrows game 0 1 1 20))))))

(deftest gain-life-test
  (let [game {:players [{:max-life 10
                         :life 1}
                        {:max-life 10
                         :life 8}]}]
    (testing "Should gain 1 life by default."
      (is (= {:players [{:max-life 10
                         :life 2}
                        {:max-life 10
                         :life 8}]}
             (gain-life game 0))))
    (testing "Should be able to gain life for multiple players."
      (is (= {:players [{:max-life 10
                         :life 2}
                        {:max-life 10
                         :life 10}]}
             (gain-life game 0 1 1 2))))
    (testing "Should not be able to gain more than max life."
      (is (= {:players [{:max-life 10
                         :life 2}
                        {:max-life 10
                         :life 10}]}
             (gain-life game 0 1 1 20))))))

(deftest lose-life-test
  (let [game {:players [{:max-life 10
                         :life 1
                         :arrows 3}
                        {:max-life 10
                         :life 8
                         :arrows 0}]
              :arrows 0}]
    (testing "Should lose 1 life by default."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 8
                         :arrows 0}]
              :arrows 3}
             (lose-life game 0))))
    (testing "Should be able to lose life for multiple players."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 6
                         :arrows 0}]
              :arrows 3}
             (lose-life game 0 1 1 2))))
    (testing "Should not be able to go below 0 life."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 0
                         :arrows 0}]
              :arrows 3}
             (lose-life game 0 1 1 20))))))

(deftest indians-attack-test
  (let [game {:players [{:max-life 10
                         :life 1
                         :arrows 1}
                        {:max-life 10
                         :life 8
                         :arrows 2}]
              :arrows 0}]
    (testing "Should attack all players by default."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 6
                         :arrows 0}]
              :arrows 3}
             (indians-attack game))))
    (testing "Should be able to attack specific players."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 8
                         :arrows 2}]
              :arrows 1}
             (indians-attack game 0))))))

(deftest gatling-gun-test
  (let [game {:players [{:max-life 10
                         :life 1
                         :arrows 1}
                        {:max-life 10
                         :life 8
                         :arrows 2}
                        {:max-life 10
                         :life 1
                         :arrows 3}]
              :active-player-idx 2
              :arrows 0}]
    (testing "Should attack all non-active players by default."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 7
                         :arrows 2}
                        {:max-life 10
                         :life 1
                         :arrows 0}]
              :active-player-idx 2
              :arrows 4}
             (gatling-gun game))))
    (testing "Should be able to attack specific players."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 8
                         :arrows 2}
                        {:max-life 10
                         :life 1
                         :arrows 0}]
              :active-player-idx 2
              :arrows 4}
             (gatling-gun game 0))))))

(deftest end-turn-test
  (rand/set-seed! 420)
  (let [game {:players [{:name "JOSE DELGADO"
                         :max-life 10
                         :life 1}
                        {:name "BART CASSIDY"
                         :max-life 10
                         :life 8}
                        {:name "TEQUILA JOE"
                         :max-life 10
                         :life 5}]
              :active-player-idx 1}]
    (testing "Should be able to find next active player."
      (is (= {:players [{:name "JOSE DELGADO"
                         :max-life 10
                         :life 1}
                        {:name "BART CASSIDY"
                         :max-life 10
                         :life 8}
                        {:name "TEQUILA JOE"
                         :max-life 10
                         :life 5}]
              :active-player-idx 2
              :dice [{:type :coward :value "BEER (2)" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "DYNAMITE" :new? true}
                     {:type :base :value "1" :new? true}]
              :dice-rolls 1}
             (end-turn game))))
    (testing "Should skip dead players."
      (is (= {:players [{:name "JOSE DELGADO"
                         :max-life 10
                         :life 1}
                        {:name "BART CASSIDY"
                         :max-life 10
                         :life 8}
                        {:name "TEQUILA JOE"
                         :max-life 10
                         :life 0}]
              :active-player-idx 0
              :dice [{:type :loudmouth :value "1 (2)" :new? true}
                     {:type :base :value "BEER" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "ARROW" :new? true}
                     {:type :base :value "DYNAMITE" :new? true}
                     {:type :base :value "1" :new? true}]
              :dice-rolls 1}
             (-> game
                 ;; kill player 2
                 (assoc-in [:players 2 :life] 0)
                 ;; end turn
                 (end-turn)))))))
