(ns repl-games.core-test
  (:require [clojure.test :refer :all]
            [repl-games.core :refer :all]
            [repl-games.random :as rand]))

(def mk-game-state #(assoc % :state (rand/shuffle* (range 10))))

(def command-map
  {:conj {:doc "conj"
          :fn #(update %1 :state conj %2)}
   :rest {:doc "rest"
          :fn #(update %1 :state rest)}})

(def command-history
  [[:setup 0] [:conj [-1]] [:rest nil] [:rest nil]])

(def game
  {:commands command-history
   :messages nil
   :state [2 5 3 6 1 4 8 9 0]})

(deftest mk-game-test
  (testing "Setting same seed should result in same outputs."
    (is (= {:commands [[:setup 0]]
            :messages nil
            :state [7 2 5 3 6 1 4 8 9 0]}
           (mk-game mk-game-state 0)
           (mk-game mk-game-state 0)))))

(deftest update-game-test
  (let [game1 (mk-game mk-game-state 0)
        game2 (update-game game1 :conj (-> command-map :conj :fn) [-1])
        game3 (update-game game2 :rest (-> command-map :rest :fn) nil)
        game4 (update-game game3 :rest (-> command-map :rest :fn) nil)]
    (testing "update-game should return updated game with new command history."
      (is (= {:commands [[:setup 0] [:conj [-1]]]
              :messages nil
              :state [-1 7 2 5 3 6 1 4 8 9 0]}
             game2))
      (is (= {:commands [[:setup 0] [:conj [-1]] [:rest nil]]
              :messages nil
              :state [7 2 5 3 6 1 4 8 9 0]}
             game3))
      (is (= game
             game4)))))

(deftest replay-commands-test
  (testing "Same command history should produce same final output."
    (is (= game
           (replay-commands command-history command-map mk-game-state)))))
