(ns btdg.commands-test
  (:require [btdg.commands :refer :all]
            [clojure.test :refer :all]
            [repl-games.random :as rand]))

(deftest reroll-dice-test
  (rand/set-seed! 420)
  (let [game (init-dice {})]
    (testing "Should reroll all dice by default."
      (is (= {:dice-rolls 2
              :dice ["ARROW" "ARROW" "2" "DYNAMITE" "1"]
              :active-die-idxs #{0 1 2 3 4}}
             (reroll-dice game))))
    (testing "Should be able to reroll specific dice."
      (is (= {:dice-rolls 2
              :dice ["BEER" "DYNAMITE" "ARROW" "DYNAMITE" "BEER"]
              :active-die-idxs #{0 2 4}}
             (reroll-dice game 0 2 4))))))

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
    (testing "Should not be able to take more arrows than game has."
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
    (testing "Should not be able to discard more arrows than player has."
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
  (let [game {:players [{:max-life 10
                         :life 1}
                        {:max-life 10
                         :life 8}
                        {:max-life 10
                         :life 5}]
              :active-player-idx 1
              :dice-rolls 2
              :dice ["ARROW" "ARROW" "2" "DYNAMITE" "1"]
              :active-die-idxs #{0 1}}]
    (testing "Should be able to find next active player and re-init dice."
      (is (= {:players [{:max-life 10
                         :life 1}
                        {:max-life 10
                         :life 8}
                        {:max-life 10
                         :life 5}]
              :active-player-idx 2
              :dice-rolls 1
              :dice ["BEER" "DYNAMITE" "DYNAMITE" "DYNAMITE" "BEER"]
              :active-die-idxs #{0 1 2 3 4}}
             (end-turn game))))
    (testing "Should skip dead players."
      (is (= {:players [{:max-life 10
                         :life 1}
                        {:max-life 10
                         :life 8}
                        {:max-life 10
                         :life 0}]
              :active-player-idx 0
              :dice-rolls 1
              :dice ["ARROW" "ARROW" "2" "DYNAMITE" "1"]
              :active-die-idxs #{0 1 2 3 4}}
             (-> game
                 (assoc-in [:players 2 :life] 0)
                 (end-turn)))))))
