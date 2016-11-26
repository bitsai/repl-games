(ns btdg.commands)

(defn- get-player-k [game player-idx k]
  (get-in game [:state :players player-idx k]))

(defn- update-player [game player-idx f & args]
  (apply update-in game [:state :players player-idx] f args))

(defn gain-life
  ([game player-idx]
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         new-life (-> (get-player-k game player-idx :life)
                      (+ n)
                      (min max-life))]
     (update-player game player-idx assoc :life new-life))))

(defn lose-life
  ([game player-idx]
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [new-life (-> (get-player-k game player-idx :life)
                      (- n)
                      (max 0))]
     (update-player game player-idx assoc :life new-life))))
