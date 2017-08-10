(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-player-k [game player-idx k]
  (get-in game [:players player-idx k]))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game [:players player-idx k] f args))

(defn- do-for-players [f game player-idx arg idxs-and-args]
  (reduce (fn [g [player-idx arg]]
             (f g player-idx arg))
           game
           (cons [player-idx arg] (partition 2 idxs-and-args))))

(defn- next-active-player-idx [game]
  (let [n (-> game :players count)]
    (->> (iterate inc 1)
         ;; generate seq of next player idxs
         (map #(-> game :active-player-idx (+ %) (mod n)))
         ;; find live player idxs
         (filter #(-> game (get-player-k % :life) pos?))
         ;; get the next one
         (first))))

(defn- roll-die [die]
  (let [roll (rand/uniform 0 6)]
    (case (:type die)
      :base (case roll
              0 "1"
              1 "2"
              2 "ARROW"
              3 "BEER"
              4 "DYNAMITE"
              5 "GATLING")
      :loudmouth (case roll
                   0 "1 (2)"
                   1 "2 (2)"
                   2 "ARROW"
                   3 "BULLET"
                   4 "DYNAMITE"
                   5 "GATLING (2)")
      :coward (case roll
                0 "1"
                1 "ARROW"
                2 "ARROW (BROKEN)"
                3 "BEER"
                4 "BEER (2)"
                5 "DYNAMITE"))))

;; commands

(defn roll-dice
  ([game]
   (let [n (-> game :dice count)]
     ;; by default, reroll all dice
     (apply roll-dice game (range n))))
  ([game & die-idxs]
   (let [die-idxs (set die-idxs)
         updated-dice (map-indexed (fn [idx die]
                                     (if-not (die-idxs idx)
                                       (dissoc die :new?)
                                       (-> die
                                           (assoc :value (roll-die die))
                                           (assoc :new? true))))
                                   (:dice game))]
     (-> game
         ;; update dice
         (assoc :dice updated-dice)
         ;; increment # dice rolls
         (update :dice-rolls inc)))))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [;; can't take more than arrows remaining
         arrows (min n (:arrows game))]
     (-> game
         (update :arrows - arrows)
         (update-player-k player-idx :arrows + arrows))))
  ([game player-idx n & args]
   (do-for-players take-arrows game player-idx n args)))

(defn discard-arrows
  ([game player-idx]
   ;; by default, discard all arrows
   (discard-arrows game player-idx (:arrow-count cfg/defaults)))
  ([game player-idx n]
   (let [player-arrows (get-player-k game player-idx :arrows)
         ;; can't discard more than arrows taken
         arrows (min n player-arrows)]
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
         ;; can't go above max life
         add-life #(min (+ %1 %2) max-life)]
     (update-player-k game player-idx :life add-life n)))
  ([game player-idx n & args]
   (do-for-players gain-life game player-idx n args)))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [;; can't go below 0 life
         remove-life #(max (- %1 %2) 0)
         updated-game (update-player-k game player-idx :life remove-life n)]
     (cond-> updated-game
       ;; if player is dead, discard arrows
       (-> updated-game (get-player-k player-idx :life) zero?)
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
     ;; by default, attack all OTHER players
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [idxs-and-hits (interleave player-idxs (repeat 1))]
     (-> game
         (#(apply lose-life % idxs-and-hits))
         (discard-arrows (:active-player-idx game))))))

(defn setup-dice [game]
  (let [active-player-idx (:active-player-idx game)
        base-dice (repeat (:dice-count cfg/defaults) {:type :base})
        dice (case (get-player-k game active-player-idx :name)
               "JOSE DELGADO" (cons {:type :loudmouth} base-dice)
               "TEQUILA JOE"  (cons {:type :coward} base-dice)
               base-dice)]
    (-> game
        ;; setup n dice
        (assoc :dice dice)
        ;; set # dice rolls to 0
        (assoc :dice-rolls 0)
        ;; make first roll
        (roll-dice))))

(defn end-turn [game]
  (-> game
      ;; find next active player
      (assoc :active-player-idx (next-active-player-idx game))
      ;; setup dice
      (setup-dice)))
