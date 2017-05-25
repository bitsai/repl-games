(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-player-k [game player-idx k]
  (get-in game (conj [:players player-idx] k)))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game (conj [:players player-idx] k) f args))

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
  (let [n (-> game :players count)]
    (->> (range 1 (inc n))
         ;; generate seq of next player idxs
         (map #(-> game :active-player-idx (+ %) (mod n)))
         ;; find live player idxs
         (filter #(-> game (get-player-k % :life) pos?))
         ;; get the next one
         (first))))

;; commands

(defn init-dice [game]
  (let [n (:dice-count cfg/defaults)]
    (-> game
        ;; set # dice rolls to 1
        (assoc :dice-rolls 1)
        ;; roll all dice
        (assoc :dice (vec (repeatedly n roll-die)))
        ;; mark all dice as active
        (assoc :active-die-idxs (set (range n))))))

(defn reroll-dice
  ([game]
   (let [n (:dice-count cfg/defaults)]
     ;; by default, reroll all dice
     (apply reroll-dice game (range n))))
  ([game & die-idxs]
   (reduce (fn [g die-idx]
             ;; roll selected die
             (assoc-in g [:dice die-idx] (roll-die)))
           (-> game
               ;; increment # dice rolls
               (update :dice-rolls inc)
               ;; mark selected dice as active
               (assoc :active-die-idxs (set die-idxs)))
           die-idxs)))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [;; can't take more than game arrows
         arrows (min n (:arrows game))]
     (-> game
         (update :arrows - arrows)
         (update-player-k player-idx :arrows + arrows))))
  ([game player-idx n & args]
   (do-for-players take-arrows game player-idx n args)))

(defn discard-arrows
  ([game player-idx]
   (let [player-arrows (get-player-k game player-idx :arrows)]
     ;; by default, discard all player arrows
     (discard-arrows game player-idx player-arrows)))
  ([game player-idx n]
   (let [player-arrows (get-player-k game player-idx :arrows)
         arrows (-> n
                    ;; can't discard more than player arrows
                    (min player-arrows))]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update :arrows + arrows))))
  ([game player-idx n & args]
   (do-for-players discard-arrows game player-idx n args)))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         add-life #(-> %
                       (+ n)
                       ;; can't go above max life
                       (min max-life))]
     (update-player-k game player-idx :life add-life)))
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
         updated (update-player-k game player-idx :life remove-life)]
     (cond-> updated
       ;; if player is dead, discard arrows
       (-> updated (get-player-k player-idx :life) zero?)
       (discard-arrows player-idx))))
  ([game player-idx n & args]
   (do-for-players lose-life game player-idx n args)))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game :players count range)]
     ;; by default, attack all players
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (let [idxs-and-arrows (->> player-idxs
                              (map #(get-player-k game % :arrows))
                              (interleave player-idxs))]
     (-> game
         (#(apply lose-life % idxs-and-arrows))
         (#(apply discard-arrows % idxs-and-arrows))))))

(defn gatling-gun
  ([game]
   (let [active-player-idx (:active-player-idx game)
         player-idxs (-> game :players count range)]
     ;; by default, attack each of the OTHER players
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [idxs-and-damages (interleave player-idxs (repeat 1))]
     (-> game
         (#(apply lose-life % idxs-and-damages))
         (discard-arrows (:active-player-idx game))))))

(defn end-turn [game]
  (-> game
      ;; find next active player
      (assoc :active-player-idx (next-active-player-idx game))
      ;; init dice again
      (init-dice)))
