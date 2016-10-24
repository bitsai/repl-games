(ns repl-games.core-test
  (:require [clojure.test :refer :all]
            [repl-games.core :refer :all]
            [repl-games.random :as rand]))

(deftest mk-game-test
  (testing "Setting same seed should result in same outputs."
    (let [mk-game-state #(assoc % :state (rand/shuffle* (range 10)))]
      (is (= {:commands [[0]]
              :messages nil
              :state [7 2 5 3 6 1 4 8 9 0]}
             (mk-game mk-game-state 0)
             (mk-game mk-game-state 0))))))

(deftest update-game-test
  (testing "Test updating world state."
    (let [cmd-map {:conj #(update %1 :state conj %2)
                   :rest #(update %1 :state rest)}
          mk-game-state #(assoc % :state (rand/shuffle* (range 10)))
          game1 (mk-game mk-game-state 0)
          game2 (update-game game1 cmd-map :conj [-1])
          game3 (update-game game2 cmd-map :rest nil)
          game4 (update-game game3 cmd-map :rest nil)]
      (is (= {:commands [[0] [:conj [-1]]]
              :messages nil
              :state [-1 7 2 5 3 6 1 4 8 9 0]}
             game2))
      (is (= {:commands [[0] [:conj [-1]] [:rest nil]]
              :messages nil
              :state [7 2 5 3 6 1 4 8 9 0]}
             game3))
      (is (= {:commands [[0] [:conj [-1]] [:rest nil] [:rest nil]]
              :messages nil
              :state [2 5 3 6 1 4 8 9 0]}
             game4)))))

(deftest replay-commands-test
  (testing "Test replaying commands."
    (let [cmds [[0] [:conj [-1]] [:rest nil] [:rest nil]]
          mk-game-state #(assoc % :state (rand/shuffle* (range 10)))
          cmd-map {:mk-game-state mk-game-state
                   :conj #(update %1 :state conj %2)
                   :rest #(update %1 :state rest)}]
      (is (= {:commands cmds
              :messages nil
              :state [2 5 3 6 1 4 8 9 0]}
             (replay-commands cmds cmd-map))))))
