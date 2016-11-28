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

(defn- do-for-players [f game player-idx n args]
  (reduce (fn [g [player-idx n]]
             (f g player-idx n))
           game
           (cons [player-idx n] (partition 2 args))))

(defn- roll-die []
  (case (rand/uniform 0 6)
    0 "ARROW"
    1 "DYNAMITE"
    2 "1"
    3 "2"
    4 "BEER"
    5 "GATLING GUN"))

(defn- next-active-player-idx [game]
  (let [n (-> game (get-in-game [:players]) count)
        active-player-idx (get-in-game game [:active-player-idx])]
    (->> (range 1 (inc n))
         ;; generate seq of next player idxs
         (map #(-> active-player-idx (+ %) (mod n)))
         ;; find live player idxs
         (filter #(-> game (get-in-player % [:life]) pos?))
         ;; get the next one
         (first))))

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
   (let [game-arrows (get-in-game game [:arrows])
         arrows (-> n
                    ;; can't take more than game arrows
                    (min game-arrows))]
     (-> game
         (update-in-game [:arrows] - arrows)
         (update-in-player player-idx [:arrows] + arrows))))
  ([game player-idx n & args]
   (do-for-players take-arrows game player-idx n args)))

(defn discard-arrows
  ([game player-idx]
   (let [player-arrows (get-in-player game player-idx [:arrows])]
     ;; by default, discard all player arrows
     (discard-arrows game player-idx player-arrows)))
  ([game player-idx n]
   (let [player-arrows (get-in-player game player-idx [:arrows])
         arrows (-> n
                    ;; can't discard more than player arrows
                    (min player-arrows))]
     (-> game
         (update-in-player player-idx [:arrows] - arrows)
         (update-in-game [:arrows] + arrows))))
  ([game player-idx n & args]
   (do-for-players discard-arrows game player-idx n args)))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-in-player game player-idx [:max-life])
         add-life #(-> %
                       (+ n)
                       ;; can't go above max life
                       (min max-life))]
     (update-in-player game player-idx [:life] add-life)))
  ([game player-idx n & args]
   (do-for-players gain-life game player-idx n args)))

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
       (discard-arrows player-idx))))
  ([game player-idx n & args]
   (do-for-players lose-life game player-idx n args)))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack all players
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (let [idxs-and-arrows (->> player-idxs
                              (map #(get-in-player game % [:arrows]))
                              (interleave player-idxs))]
     (-> game
         (#(apply lose-life % idxs-and-arrows))
         (#(apply discard-arrows % idxs-and-arrows))))))

(defn gatling-gun
  ([game]
   (let [active-player-idx (get-in-game game [:active-player-idx])
         player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack each of the OTHER players
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [active-player-idx (get-in-game game [:active-player-idx])
         idxs-and-damages (interleave player-idxs (repeat 1))]
     (-> game
         (#(apply lose-life % idxs-and-damages))
         (discard-arrows active-player-idx)))))

(defn end-turn [game]
  (-> game
      ;; find next active player
      (assoc-in-game [:active-player-idx] (next-active-player-idx game))
      ;; init dice again
      (init-dice)))
