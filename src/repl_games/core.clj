(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-world-state [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; mk-game-state should update :game-state and :message-queue
  (mk-game-state {:command-log [[seed]]
                  :game-state nil
                  :message-queue nil}))

(defn mk-help [{:keys [ns help-atom]}]
  ;; create help function in target namespace
  (intern ns 'help (fn []
                     (doseq [[x y] @help-atom]
                       (println x y))))
  ;; add doc string to help atom
  (swap! help-atom conj ["help" "(help)"]))

(defn mk-setup [{:keys [ns help-atom world-atom doc mk-game-state]}]
  ;; create setup function in target namespace
  (intern ns 'su (fn []
                   (->> (System/currentTimeMillis)
                        (mk-world-state mk-game-state)
                        (reset! world-atom))))
  ;; add doc string to help atom
  (swap! help-atom conj ["su" "(setup)"]))

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
        (assoc :message-queue nil))))
