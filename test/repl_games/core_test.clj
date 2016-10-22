(ns repl-games.core-test
  (:require [clojure.test :refer :all]
            [repl-games.core :refer :all]
            [repl-games.random :as rand]))

(deftest mk-world-state-test
  (testing "Setting same seed should result in same outputs."
    (let [mk-game-state #(assoc % :game-state (rand/shuffle* (range 10)))]
      (is (= {:command-log [[0]]
              :game-state [7 2 5 3 6 1 4 8 9 0]
              :message-queue nil}
             (mk-world-state mk-game-state 0)
             (mk-world-state mk-game-state 0))))))

(deftest update-world-state-test
  (testing "Test updating world state."
    (let [cmd-map {:conj #(update %1 :game-state conj %2)
                   :rest #(update %1 :game-state rest)}
          mk-game-state #(assoc % :game-state (rand/shuffle* (range 10)))
          world-state1 (mk-world-state mk-game-state 0)
          world-state2 (update-world-state world-state1 cmd-map :conj [-1])
          world-state3 (update-world-state world-state2 cmd-map :rest nil)
          world-state4 (update-world-state world-state3 cmd-map :rest nil)]
      (is (= {:command-log [[0] [:conj [-1]]]
              :game-state [-1 7 2 5 3 6 1 4 8 9 0]
              :message-queue nil}
             world-state2))
      (is (= {:command-log [[0] [:conj [-1]] [:rest nil]]
              :game-state [7 2 5 3 6 1 4 8 9 0]
              :message-queue nil}
             world-state3))
      (is (= {:command-log [[0] [:conj [-1]] [:rest nil] [:rest nil]]
              :game-state [2 5 3 6 1 4 8 9 0]
              :message-queue nil}
             world-state4)))))

(deftest replay-commands-test
  (testing "Test replaying commands."
    (let [cmd-log [[0] [:conj [-1]] [:rest nil] [:rest nil]]
          mk-game-state #(assoc % :game-state (rand/shuffle* (range 10)))
          cmd-map {:mk-game-state mk-game-state
                   :conj #(update %1 :game-state conj %2)
                   :rest #(update %1 :game-state rest)}]
      (is (= {:command-log cmd-log
              :game-state [2 5 3 6 1 4 8 9 0]
              :message-queue nil}
             (replay-commands cmd-log cmd-map))))))
