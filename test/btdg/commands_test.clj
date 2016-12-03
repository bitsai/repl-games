(ns btdg.commands-test
  (:require [btdg.commands :refer :all]
            [clojure.test :refer :all]
            [repl-games.random :as rand]))

(deftest init-dice-test
  (rand/set-seed! 420)
  (testing "Should be able to init dice."
    (is (= {:state {:dice-rolls 1
                    :dice ["BEER" "DYNAMITE" "DYNAMITE" "DYNAMITE" "BEER"]
                    :active-die-idxs #{0 1 2 3 4}}}
           (init-dice {})))))

(deftest reroll-dice-test
  (rand/set-seed! 420)
  (let [game (init-dice {})]
    (testing "Should reroll all dice by default."
      (is (= {:state {:dice-rolls 2
                      :dice ["ARROW" "ARROW" "2" "DYNAMITE" "1"]
                      :active-die-idxs #{0 1 2 3 4}}}
             (reroll-dice game))))
    (testing "Should be able to reroll specific dice."
      (is (= {:state {:dice-rolls 2
                      :dice ["BEER" "DYNAMITE" "ARROW" "DYNAMITE" "BEER"]
                      :active-die-idxs #{0 2 4}}}
             (reroll-dice game 0 2 4))))))

(deftest take-arrows-test
  (let [game {:state {:players [{:arrows 0}
                                {:arrows 0}]
                      :arrows 9}}]
    (testing "Should take 1 arrow by default."
      (is (= {:state {:players [{:arrows 1}
                                {:arrows 0}]
                      :arrows 8}}
             (take-arrows game 0))))
    (testing "Should be able to have multiple players take arrows."
      (is (= {:state {:players [{:arrows 1}
                                {:arrows 2}]
                      :arrows 6}}
             (take-arrows game 0 1 1 2))))
    (testing "Should not be able to take more arrows than exists in game."
      (is (= {:state {:players [{:arrows 1}
                                {:arrows 8}]
                      :arrows 0}}
             (take-arrows game 0 1 1 20))))))

(deftest discard-arrows-test
  (let [game {:state {:players [{:arrows 1}
                                {:arrows 8}]
                      :arrows 0}}]
    (testing "Should discard all player arrows by default."
      (is (= {:state {:players [{:arrows 1}
                                {:arrows 0}]
                      :arrows 8}}
             (discard-arrows game 1))))
    (testing "Should be able to have multiple players discard arrows."
      (is (= {:state {:players [{:arrows 0}
                                {:arrows 6}]
                      :arrows 3}}
             (discard-arrows game 0 1 1 2))))
    (testing "Should not be able to discard more arrows than exists in player."
      (is (= {:state {:players [{:arrows 0}
                                {:arrows 0}]
                      :arrows 9}}
             (discard-arrows game 0 1 1 20))))))
