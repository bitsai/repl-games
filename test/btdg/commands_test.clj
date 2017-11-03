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
    (testing "Roll all dice."
      (is (= {:dice [{:type :base :value "Indian arrow" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :loudmouth :value "2 (x2)" :new? true}
                     {:type :coward :value "Indian arrow" :new? true}]
              :dice-rolls 1}
             (roll-dice game 0 1 2 3 4))))
    (testing "Roll specified dice."
      (is (= {:dice [{:type :base :value "1" :new? true}
                     {:type :base}
                     {:type :base :value "1" :new? true}
                     {:type :loudmouth}
                     {:type :coward :value "Broken arrow" :new? true}]
              :dice-rolls 1}
             (roll-dice game 0 2 4))))))

(deftest take-arrows-test
  (let [game {:players [{:arrows 0}
                        {:arrows 0}]
              :active-player-idx 0
              :arrows 9}]
    (testing "By default, active player takes 1 arrow."
      (is (= {:players [{:arrows 1}
                        {:arrows 0}]
              :active-player-idx 0
              :arrows 8}
             (take-arrows game))))
    (testing "Active player takes n arrows."
      (is (= {:players [{:arrows 2}
                        {:arrows 0}]
              :active-player-idx 0
              :arrows 7}
             (take-arrows game 2))))
    (testing "Can't take more than arrows remaining."
      (is (= {:players [{:arrows 9}
                        {:arrows 0}]
              :active-player-idx 0
              :arrows 0}
             (take-arrows game 20))))
    (testing "Specified player takes n arrows."
      (is (= {:players [{:arrows 0}
                        {:arrows 2}]
              :active-player-idx 0
              :arrows 7}
             (take-arrows game 1 2))))))

(deftest discard-arrows-test
  (let [game {:players [{:arrows 3}
                        {:arrows 3}]
              :active-player-idx 0
              :arrows 3}]
    (testing "By default, active player discards all arrows."
      (is (= {:players [{:arrows 0}
                        {:arrows 3}]
              :active-player-idx 0
              :arrows 6}
             (discard-arrows game))))
    (testing "Active player discards n arrows."
      (is (= {:players [{:arrows 1}
                        {:arrows 3}]
              :active-player-idx 0
              :arrows 5}
             (discard-arrows game 2))))
    (testing "Can't discard more than arrows taken."
      (is (= {:players [{:arrows 0}
                        {:arrows 3}]
              :active-player-idx 0
              :arrows 6}
             (discard-arrows game 20))))
    (testing "Specified player discards n arrows."
      (is (= {:players [{:arrows 3}
                        {:arrows 1}]
              :active-player-idx 0
              :arrows 5}
             (discard-arrows game 1 2))))))

(deftest gain-life-test
  (let [game {:players [{:max-life 10
                         :life 1}
                        {:max-life 10
                         :life 1}]
              :active-player-idx 0}]
    (testing "By default, active player gains 1 life."
      (is (= {:players [{:max-life 10
                         :life 2}
                        {:max-life 10
                         :life 1}]
              :active-player-idx 0}
             (gain-life game))))
    (testing "Active player gains n life."
      (is (= {:players [{:max-life 10
                         :life 3}
                        {:max-life 10
                         :life 1}]
              :active-player-idx 0}
             (gain-life game 2))))
    (testing "Can't go above max life."
      (is (= {:players [{:max-life 10
                         :life 10}
                        {:max-life 10
                         :life 1}]
              :active-player-idx 0}
             (gain-life game 20))))
    (testing "Specified players gain n life."
      (is (= {:players [{:max-life 10
                         :life 2}
                        {:max-life 10
                         :life 3}]
              :active-player-idx 0}
             (gain-life game 0 1 1 2))))))

(deftest lose-life-test
  (let [game {:players [{:max-life 10
                         :life 10
                         :arrows 3}
                        {:max-life 10
                         :life 10}]
              :active-player-idx 0
              :arrows 6}]
    (testing "By default, active player loses 1 life."
      (is (= {:players [{:max-life 10
                         :life 9
                         :arrows 3}
                        {:max-life 10
                         :life 10}]
              :active-player-idx 0
              :arrows 6}
             (lose-life game))))
    (testing "Active player loses n life."
      (is (= {:players [{:max-life 10
                         :life 8
                         :arrows 3}
                        {:max-life 10
                         :life 10}]
              :active-player-idx 0
              :arrows 6}
             (lose-life game 2))))
    (testing "Can't go below 0 life."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 10}]
              :active-player-idx 0
              :arrows 9}
             (lose-life game 20))))
    (testing "Specified players lose n life."
      (is (= {:players [{:max-life 10
                         :life 9
                         :arrows 3}
                        {:max-life 10
                         :life 8}]
              :active-player-idx 0
              :arrows 6}
             (lose-life game 0 1 1 2))))))

(deftest indians-attack-test
  (let [game {:players [{:max-life 10
                         :life 1
                         :arrows 3}
                        {:max-life 10
                         :life 1
                         :arrows 3}]
              :active-player-idx 0
              :arrows 3}]
    (testing "By default, attack all players."
      (is (= {:players [{:max-life 10
                         :life 0
                         :arrows 0}
                        {:max-life 10
                         :life 0
                         :arrows 0}]
              :active-player-idx 0
              :arrows 9}
             (indians-attack game))))
    (testing "Exclude specified players."
      (is (= {:players [{:max-life 10
                         :life 1
                         :arrows 3}
                        {:max-life 10
                         :life 0
                         :arrows 0}]
              :active-player-idx 0
              :arrows 6}
             (indians-attack game 0))))))

(deftest gatling-gun-test
  (let [game {:players [{:max-life 10
                         :life 10
                         :arrows 3}
                        {:max-life 10
                         :life 10}
                        {:max-life 10
                         :life 10}]
              :active-player-idx 0
              :arrows 6}]
    (testing "By default, shoot all OTHER players."
      (is (= {:players [{:max-life 10
                         :life 10
                         :arrows 0}
                        {:max-life 10
                         :life 9}
                        {:max-life 10
                         :life 9}]
              :active-player-idx 0
              :arrows 9}
             (gatling-gun game))))
    (testing "Exclude specified players (and active player)."
      (is (= {:players [{:max-life 10
                         :life 10
                         :arrows 0}
                        {:max-life 10
                         :life 10}
                        {:max-life 10
                         :life 9}]
              :active-player-idx 0
              :arrows 9}
             (gatling-gun game 1))))))

(deftest end-turn-test
  (rand/set-seed! 420)
  (let [game {:players [{:name "JOSE DELGADO"
                         :max-life 9
                         :life 9}
                        {:name "BART CASSIDY"
                         :max-life 8
                         :life 8}
                        {:name "BLACK JACK"
                         :max-life 8
                         :life 8}]
              :active-player-idx 1}]
    (testing "Find next active player."
      (is (= {:players [{:name "JOSE DELGADO"
                         :max-life 9
                         :life 9}
                        {:name "BART CASSIDY"
                         :max-life 8
                         :life 8}
                        {:name "BLACK JACK"
                         :max-life 8
                         :life 8}]
              :active-player-idx 2
              :dice [{:type :base :value "Indian arrow" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "Indian arrow" :new? true}]
              :dice-rolls 1}
             (end-turn game))))
    (testing "Skip dead players."
      (is (= {:players [{:name "JOSE DELGADO"
                         :max-life 9
                         :life 9}
                        {:name "BART CASSIDY"
                         :max-life 8
                         :life 8}
                        {:name "BLACK JACK"
                         :max-life 8
                         :life 0}]
              :active-player-idx 0
              :dice [{:type :loudmouth :value "1 (x2)" :new? true}
                     {:type :base :value "1" :new? true}
                     {:type :base :value "Fan the Hammer" :new? true}
                     {:type :base :value "2" :new? true}
                     {:type :base :value "Bandage" :new? true}
                     {:type :base :value "Indian arrow" :new? true}]
              :dice-rolls 1}
             (-> game
                 ;; kill player 2
                 (assoc-in [:players 2 :life] 0)
                 ;; end turn
                 (end-turn)))))))
