(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-world-state [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; make state
  {:command-log [[seed]]
   :game-state (mk-game-state)
   :message-queue []})

(defn update-world-state [world-state cmd-map cmd-name args]
  (let [cmd-fn (-> cmd-map cmd-name)]
    ;; iff a new state is produced ...
    (when-let [s (apply cmd-fn world-state args)]
      ;; update command log
      (update s :command-log conj [cmd-name args]))))

(defn replay-commands [cmd-log cmd-map]
  ;; first log entry contains data for making world state
  (let [[seed] (first cmd-log)]
    ;; replay commands
    (-> (reduce (fn [world-state [cmd-name args]]
                  (update-world-state world-state cmd-map cmd-name args))
                ;; look up mk-game-state, initialize world state
                (mk-world-state (-> cmd-map :mk-game-state) seed)
                (rest cmd-log))
        ;; ignore messages when replaying
        (assoc :message-queue []))))


