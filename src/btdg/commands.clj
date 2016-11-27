(ns btdg.commands
  (:require [btdg.config :as cfg]))

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

(defn- roll-die []
  (case (rand-int 6)
    0 "ARROW"
    1 "DYNAMITE"
    2 "1"
    3 "2"
    4 "BEER"
    5 "GATLING GUN"))

;; commands

(defn init-dice [game]
  (let [n (:dice-count cfg/defaults)]
    (-> game
        ;; set # dice rolls to 1
        (assoc-in [:state :dice-rolls] 1)
        ;; roll all dice
        (assoc-in [:state :dice] (vec (repeatedly n roll-die)))
        ;; mark all dice as active
        (assoc-in [:state :active-die-idxs] (set (range n))))))

(defn roll-dice
  ([game]
   (let [n (:dice-count cfg/defaults)]
     ;; by default, roll all dice
     (apply roll-dice game (range n))))
  ([game & die-idxs]
   (reduce (fn [g die-idx]
             ;; roll selected die
             (assoc-in g [:state :dice die-idx] (roll-die)))
           (-> game
               ;; increment # dice rolls
               (update-in [:state :dice-rolls] inc)
               ;; mark selected dice as active
               (assoc-in [:state :active-die-idxs] (set die-idxs)))
           die-idxs)))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [arrows (-> n
                    ;; can't take more arrows than are left
                    (min (get-in game [:state :arrows])))]
     (-> game
         (update-in [:state :arrows] - arrows)
         (update-player-k player-idx :arrows + arrows)))))

(defn discard-arrows
  ([game player-idx]
   (let [arrows (get-player-k game player-idx :arrows)]
     ;; by default, discard all arrows
     (discard-arrows game player-idx arrows)))
  ([game player-idx n]
   (let [arrows (-> n
                    ;; can't discard more arrows than player has
                    (min (get-player-k game player-idx :arrows)))]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update-in [:state :arrows] + arrows)))))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         add-life #(-> %
                       (+ n)
                       ;; can't gain more than player max life
                       (min max-life))]
     (update-player-k game player-idx :life add-life))))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [remove-life #(-> %
                          (- n)
                          ;; can't go below 0 life
                          (max 0))
         updated (update-player-k game player-idx :life remove-life)]
     (cond-> updated
       ;; if player is dead, discard arrows and remove from game
       (-> updated (get-player-k player-idx :life) zero?)
       (-> (discard-arrows player-idx)
           (remove-player player-idx))))))

(defn indians-attack
  ([game]
   (let [player-idxs (get-player-idxs game)]
     ;; by default, attack all players
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
   (let [active-player-idx (-> game :state :active-player-idx)
         player-idxs (->> game get-player-idxs (remove #{active-player-idx}))]
     ;; by default, attack each OTHER player
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
    (-> game
        ;; set next player as active
        (update-in [:state :active-player-idx] #(-> % inc (mod n)))
        ;; re-init dice
        (init-dice))))
