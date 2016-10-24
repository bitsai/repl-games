(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-game [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; mk-game-state should update :messages and :state
  (mk-game-state {:commands [[seed]]
                  :messages nil
                  :state nil}))

(defn update-game [game cmd-map cmd-name args]
  (let [cmd-fn (-> cmd-map cmd-name)]
    ;; iff an updated game is produced ...
    (when-let [g (apply cmd-fn game args)]
      ;; update command log
      (update g :commands conj [cmd-name args]))))

(defn replay-commands [cmds cmd-map]
  ;; first command contains data for making new game
  (let [[seed] (first cmds)]
    ;; replay commands
    (-> (reduce (fn [game [cmd-name args]]
                  (update-game game cmd-map cmd-name args))
                ;; look up mk-game-state, initialize game
                (mk-game (-> cmd-map :mk-game-state) seed)
                (rest cmds))
        ;; messages should be ignored during replay
        (assoc :messages nil))))

(defn mk-help-setup-undo [{:keys [ns help-atom game-atom command-map]}]
  ;; create help function
  (intern ns 'help #(doseq [[x y] @help-atom]
                      (println x y)))
  ;; add help to help
  (swap! help-atom conj ["help" "(help)"])
  ;; create setup function
  (intern ns 'su #(->> (System/currentTimeMillis)
                       (mk-game (:mk-game-state command-map))
                       (reset! game-atom)
                       ((:pr-game command-map))))
  ;; add setup to help
  (swap! help-atom conj ["su" "(setup)"])
  ;; create undo function
  (intern ns 'un #(let [cmds (-> @game-atom :commands)]
                    ;; allow undo iff there is at least 1 non-setup command
                    (when (-> cmds count (> 1))
                      (->> (replay-commands (butlast cmds) command-map)
                           (reset! game-atom)
                           ((:pr-game command-map))))))
  ;; add undo to help
  (swap! help-atom conj ["un" "(undo)"]))
