(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-world-state [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; mk-game-state should update :game-state and :message-queue
  (mk-game-state {:command-log [[seed]]
                  :game-state nil
                  :message-queue nil}))

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
        ;; messages should be ignored during replay
        (assoc :message-queue nil))))

(defn mk-help-setup-undo [{:keys [ns help-atom world-atom command-map]}]
  ;; create help function
  (intern ns 'help #(doseq [[x y] @help-atom]
                      (println x y)))
  ;; add help to help
  (swap! help-atom conj ["help" "(help)"])
  ;; create setup function
  (intern ns 'su #(->> (System/currentTimeMillis)
                       (mk-world-state (:mk-game-state command-map))
                       (reset! world-atom)
                       ;; print new world state
                       ((:pr-world-state command-map))))
  ;; add setup to help
  (swap! help-atom conj ["su" "(setup)"])
  ;; create undo function
  (intern ns 'un #(let [cmd-log (-> @world-atom :command-log)]
                    ;; allow undo iff there is at least 1 non-setup command
                    (when (-> cmd-log (count) (> 1))
                      (->> (replay-commands (butlast cmd-log) command-map)
                           (reset! world-atom)
                           ;; print new world state
                           ((:pr-world-state command-map))))))
  ;; add undo to help
  (swap! help-atom conj ["un" "(undo)"]))
