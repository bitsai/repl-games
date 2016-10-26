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

(defn replay-commands [cmds cmd-map mk-game-state]
  ;; first command contains data for making new game
  (let [[seed] (first cmds)]
    ;; replay commands
    (-> (reduce (fn [game [cmd-name args]]
                  (let [cmd-fn (-> cmd-map cmd-name :fn)]
                    (update-game game cmd-name cmd-fn args)))
                (mk-game mk-game-state seed)
                (rest cmds))
        ;; messages should be ignored during replay
        (assoc :messages nil))))

(defn print-and-reset! [game print-game game-atom]
  (print-game game)
  (-> game
      ;; discard printed messages
      (assoc :messages nil)
      ;; update game atom
      (->> (reset! game-atom)))
  nil)

(defn mk-command! [ns game-atom help-atom cmd-name cmd-spec print-game]
  (swap! help-atom conj [(name cmd-name) (:doc cmd-spec)])
  (intern ns
          (-> cmd-name name symbol)
          (fn [& args]
            (when-let [g (update-game @game-atom cmd-name (:fn cmd-spec) args)]
              (print-and-reset! g print-game game-atom)))))

(defn mk-commands! [ns game-atom help-atom meta-fn-map cmd-map]
  (let [{:keys [mk-game-state print-game]} meta-fn-map]
    ;; create help command
    (swap! help-atom conj ["help" "(help)"])
    (intern ns 'help #(doseq [[x y] @help-atom]
                        (println x y)))
    ;; create setup command
    (swap! help-atom conj ["su" "(setup)"])
    (intern ns 'su #(-> (mk-game mk-game-state (System/currentTimeMillis))
                        (print-and-reset! print-game game-atom)))

    ;; create undo command
    (swap! help-atom conj ["un" "(undo)"])
    (intern ns 'un #(let [cmds (-> @game-atom :commands butlast)]
                      ;; allow undo iff there is at least 1 command to replay
                      (when (-> cmds count pos?)
                        (-> (replay-commands cmds cmd-map mk-game-state)
                            (print-and-reset! print-game game-atom)))))
    ;; create game commands
    (doseq [[cmd-name cmd-spec] cmd-map]
      (mk-command! ns game-atom help-atom cmd-name cmd-spec print-game))))
