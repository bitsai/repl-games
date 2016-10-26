(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-game [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; mk-game-state should update :messages and :state
  (mk-game-state {:commands [[seed]]
                  :messages nil
                  :state nil}))

(defn update-game [game cmd-name cmd-fn args]
  ;; iff an updated game is produced ...
  (when-let [g (apply cmd-fn game args)]
    ;; update command log
    (update g :commands conj [cmd-name args])))

(defn replay-commands [cmds cmd-map]
  ;; first command contains data for making new game
  (let [[seed] (first cmds)]
    ;; replay commands
    (-> (reduce (fn [game [cmd-name args]]
                  (let [cmd-fn (-> cmd-map cmd-name :fn)]
                    (update-game game cmd-name cmd-fn args)))
                ;; look up mk-game-state, initialize game
                (mk-game (-> cmd-map :mk-game-state) seed)
                (rest cmds))
        ;; messages should be ignored during replay
        (assoc :messages nil))))

(defn mk-command [ns game-atom help-atom cmd-name cmd-spec print-game]
  (swap! help-atom conj [(name cmd-name) (:doc cmd-spec)])
  (intern ns
          (-> cmd-name name symbol)
          (fn [& args]
            (when-let [g (update-game @game-atom cmd-name (:fn cmd-spec) args)]
              (print-game g)))))

(defn mk-commands [ns game-atom help-atom command-map mk-game-state print-game]
  ;; create help command
  (swap! help-atom conj ["help" "(help)"])
  (intern ns 'help #(doseq [[x y] @help-atom]
                      (println x y)))
  ;; create setup command
  (swap! help-atom conj ["su" "(setup)"])
  (intern ns 'su #(->> (System/currentTimeMillis)
                       (mk-game mk-game-state)
                       (reset! game-atom)
                       (print-game)))

  ;; create undo command (TODO: test!)
  (swap! help-atom conj ["un" "(undo)"])
  (intern ns 'un #(let [cmds (-> @game-atom :commands)]
                    ;; allow undo iff there is at least 1 non-setup command
                    (when (-> cmds count (> 1))
                      (->> (replay-commands (butlast cmds) command-map)
                           (reset! game-atom)
                           (print-game)))))
  ;; create game commands
  (doseq [[cmd-name cmd-spec] command-map]
    (mk-command ns game-atom help-atom cmd-name cmd-spec print-game)))
