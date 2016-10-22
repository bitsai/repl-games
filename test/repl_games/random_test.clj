(ns repl-games.random-test
    (:require [clojure.test :refer :all]
              [repl-games.random :refer :all]))

(deftest shuffle*-test
  (testing "Setting seed should result in repeatable results."
    (set-seed! 0)
    (is (= [7 2 5 3 6 1 4 8 9 0]
           (shuffle* (range 10))))
    (is (= [9 2 1 7 0 8 3 5 6 4]
           (shuffle* (range 10))))
    (is (= [4 6 7 3 9 8 5 2 1 0]
           (shuffle* (range 10))))))
