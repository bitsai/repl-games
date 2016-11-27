(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-in-game [game ks]
  (get-in game (cons :state ks)))

(defn- assoc-in-game [game ks v]
  (assoc-in game (cons :state ks) v))

(defn- update-in-game [game ks f & args]
  (apply update-in game (cons :state ks) f args))

(defn- get-in-player [game player-idx ks]
  (get-in-game game (concat [:players player-idx] ks)))

(defn- assoc-in-player [game player-idx ks v]
  (assoc-in-game game (concat [:players player-idx] ks) v))

(defn- update-in-player [game player-idx ks f & args]
  (apply update-in-game game (concat [:players player-idx] ks) f args))

(defn- roll-die []
  (case (rand/uniform 0 6)
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
        (assoc-in-game [:dice-rolls] 1)
        ;; roll all dice
        (assoc-in-game [:dice] (vec (repeatedly n roll-die)))
        ;; mark all dice as active
        (assoc-in-game [:active-die-idxs] (set (range n))))))

(defn roll-dice
  ([game]
   (let [n (:dice-count cfg/defaults)]
     ;; by default, roll all dice
     (apply roll-dice game (range n))))
  ([game & die-idxs]
   (reduce (fn [g die-idx]
             ;; roll selected die
             (assoc-in-game g [:dice die-idx] (roll-die)))
           (-> game
               ;; increment # dice rolls
               (update-in-game [:dice-rolls] inc)
               ;; mark selected dice as active
               (assoc-in-game [:active-die-idxs] (set die-idxs)))
           die-idxs)))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [arrows (-> n
                    ;; can't take more arrows than are left
                    (min (get-in-game game [:arrows])))]
     (-> game
         (update-in-game [:arrows] - arrows)
         (update-in-player player-idx [:arrows] + arrows)))))

(defn discard-arrows
  ([game player-idx]
   (let [arrows (get-in-player game player-idx [:arrows])]
     ;; by default, discard all arrows
     (discard-arrows game player-idx arrows)))
  ([game player-idx n]
   (let [arrows (-> n
                    ;; can't discard more arrows than player has
                    (min (get-in-player game player-idx [:arrows])))]
     (-> game
         (update-in-player player-idx [:arrows] - arrows)
         (update-in-game [:arrows] + arrows)))))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-in-player game player-idx [:max-life])
         add-life #(-> %
                       (+ n)
                       ;; can't gain more than player max life
                       (min max-life))]
     (update-in-player game player-idx [:life] add-life))))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [remove-life #(-> %
                          (- n)
                          ;; can't go below 0 life
                          (max 0))
         updated (update-in-player game player-idx [:life] remove-life)]
     (cond-> updated
       ;; if player is dead, discard arrows
       (-> updated (get-in-player player-idx [:life]) zero?)
       (discard-arrows player-idx)))))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack all players
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (reduce (fn [g player-idx]
             (let [arrows (get-in-player g player-idx [:arrows])]
               (-> g
                   (discard-arrows player-idx)
                   (lose-life player-idx arrows))))
           game
           player-idxs)))

(defn gatling-gun
  ([game]
   (let [active-player-idx (get-in-game game [:active-player-idx])
         player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack each OTHER player
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [active-player-idx (get-in-game game [:active-player-idx])]
     (reduce (fn [g player-idx]
               (lose-life g player-idx))
             ;; discard arrows for active player
             (discard-arrows game active-player-idx)
             player-idxs))))

(defn end-turn [game]
  (let [n (-> game (get-in-game [:players]) count)
        ;; set next player as active
        updated (update-in-game game [:active-player-idx] #(-> % inc (mod n)))
        ;; get new active player idx
        active-player-idx (get-in-game updated [:active-player-idx])]
    (cond-> updated
      ;; re-init dice iff new active player is alive
      (-> updated (get-in-player active-player-idx [:life]) pos?)
      (init-dice))))
