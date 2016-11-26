(ns btdg.commands)

(defn- get-player-k [game player-idx k]
  (get-in game [:state :players player-idx k]))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game [:state :players player-idx k] f args))

(defn take-arrows
  ([game player-idx]
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [arrows (-> (get-in game [:state :arrows])
                    (min n))]
     (-> game
         (update-in [:state :arrows] - arrows)
         (update-player-k player-idx :arrows + arrows)))))

(defn discard-arrows
  ([game player-idx]
   (let [arrows (get-player-k game player-idx :arrows)]
     (discard-arrows game player-idx arrows)))
  ([game player-idx n]
   (let [arrows (-> (get-player-k game player-idx :arrows)
                    (min n))]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update-in [:state :arrows] + arrows)))))

(defn gain-life
  ([game player-idx]
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         add-life #(-> %
                       (+ n)
                       (min max-life))]
     (update-player-k game player-idx :life add-life))))

(defn lose-life
  ([game player-idx]
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [remove-life #(-> %
                          (- n)
                          (max 0))]
     (update-player-k game player-idx :life remove-life))))
