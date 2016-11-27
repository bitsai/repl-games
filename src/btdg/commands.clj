(ns btdg.commands)

;; helpers

(defn- get-player-idxs [game]
  (-> game :state :players count range))

(defn- get-player-k [game player-idx k]
  (get-in game [:state :players player-idx k]))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game [:state :players player-idx k] f args))

(defn- remove-player [game player-idx]
  (update-in game [:state :players] #(->> (assoc % player-idx nil)
                                          (remove nil?)
                                          (vec))))

;; commands

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [;; can't take more arrows than are left
         arrows (-> (get-in game [:state :arrows])
                    (min n))]
     (-> game
         (update-in [:state :arrows] - arrows)
         (update-player-k player-idx :arrows + arrows)))))

(defn discard-arrows
  ([game player-idx]
   ;; by default, discard all arrows
   (let [arrows (get-player-k game player-idx :arrows)]
     (discard-arrows game player-idx arrows)))
  ([game player-idx n]
   (let [;; can't discard more arrows than player has
         arrows (-> (get-player-k game player-idx :arrows)
                    (min n))]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update-in [:state :arrows] + arrows)))))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [;; can't gain more than player max life
         max-life (get-player-k game player-idx :max-life)
         add-life #(-> %
                       (+ n)
                       (min max-life))]
     (update-player-k game player-idx :life add-life))))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [;; can't go below 0 life
         remove-life #(-> %
                          (- n)
                          (max 0))
         updated (update-player-k game player-idx :life remove-life)]
     (cond-> updated
       ;; if player is dead, discard arrows and remove from game
       (-> updated (get-player-k player-idx :life) zero?)
       (-> (discard-arrows player-idx)
           (remove-player player-idx))))))

(defn indians-attack
  ([game]
   ;; by default, attack all players
   (let [player-idxs (get-player-idxs game)]
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (reduce (fn [g player-idx]
             (let [arrows (get-player-k g player-idx :arrows)]
               (-> g
                   (discard-arrows player-idx)
                   (lose-life player-idx arrows))))
           game
           player-idxs)))

(defn gatling-gun
  ([game]
   ;; by default, attack each OTHER player
   (let [active-player-idx (-> game :state :active-player-idx)
         player-idxs (->> game get-player-idxs (remove #{active-player-idx}))]
     (apply gatling-gun game player-idxs)))
  ([game & player-idxs]
   (let [active-player-idx (-> game :state :active-player-idx)]
     (reduce (fn [g player-idx]
               (lose-life g player-idx))
             ;; discard arrows for active player
             (discard-arrows game active-player-idx)
             player-idxs))))

(defn end-turn [game]
  (let [n (-> game :state :players count)]
    (update-in game [:state :active-player-idx] #(-> %
                                                     (inc)
                                                     (mod n)))))
