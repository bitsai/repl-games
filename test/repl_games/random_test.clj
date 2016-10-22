(ns repl-games.random-test
    (:require [clojure.test :refer :all]
              [repl-games.random :refer :all]))

(deftest shuffle*-test
  (testing "Setting same seed should result in same outputs."
    (set-seed! 0)
    (is (= [7 2 5 3 6 1 4 8 9 0]
           (shuffle* (range 10))))
    (set-seed! 0)
    (is (= [7 2 5 3 6 1 4 8 9 0]
           (shuffle* (range 10))))
    (set-seed! 0)
    (is (= [7 2 5 3 6 1 4 8 9 0]
           (shuffle* (range 10))))))
