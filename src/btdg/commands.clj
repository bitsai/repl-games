(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-player-k [game player-idx k]
  (get-in game [:players player-idx k]))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game [:players player-idx k] f args))

(defn- do-for-players [game f player-idxs-and-args]
  (reduce (fn [g [player-idx arg]]
             (f g player-idx arg))
           game
           (partition 2 player-idxs-and-args)))

(defn- next-active-player-idx [game]
  (let [n (-> game :players count)]
    (->> (iterate inc 1)
         (map #(-> game :active-player-idx (+ %) (mod n)))
         (filter #(-> game (get-player-k % :life) pos?))
         (first))))

(defn- roll-die [die]
  (-> die :type cfg/dice rand/rand-nth*))

;; commands

(defn roll-dice [game & die-idxs]
  (let [die-idxs (set die-idxs)
        ;; use mapv to evaluate immediately, to avoid bad randomness
        updated-dice (mapv (fn [idx die]
                             (if-not (die-idxs idx)
                               (dissoc die :new?)
                               (-> die
                                   (assoc :value (roll-die die))
                                   (assoc :new? true))))
                           (range)
                           (:dice game))]
    (-> game
        (assoc :dice updated-dice)
        (update :dice-rolls inc))))

(defn take-arrows
  ([game n]
   (take-arrows game (:active-player-idx game) n))
  ([game player-idx n]
   (let [;; can't take more than arrows remaining
         arrows (min n (:arrows game))]
     (-> game
         (update :arrows - arrows)
         (update-player-k player-idx :arrows + arrows)))))

(defn discard-arrows
  ([game n]
   (discard-arrows game (:active-player-idx game) n))
  ([game player-idx n]
   (let [player-arrows (get-player-k game player-idx :arrows)
         ;; can't discard more than arrows taken
         arrows (min n player-arrows)]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update :arrows + arrows)))))

(defn gain-life
  ([game n]
   (gain-life game (:active-player-idx game) n))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         ;; can't go above max life
         add-life #(min (+ %1 %2) max-life)]
     (update-player-k game player-idx :life add-life n)))
  ([game player-idx n & args]
   (do-for-players game gain-life (concat [player-idx n] args))))

(defn lose-life
  ([game n]
   (lose-life game (:active-player-idx game) n))
  ([game player-idx n]
   (let [;; can't go below 0 life
         remove-life #(max (- %1 %2) 0)
         updated-game (update-player-k game player-idx :life remove-life n)]
     (cond-> updated-game
       ;; if player is dead, discard their arrows
       (-> updated-game (get-player-k player-idx :life) zero?)
       (discard-arrows player-idx (:arrow-count cfg/defaults)))))
  ([game player-idx n & args]
   (do-for-players game lose-life (concat [player-idx n] args))))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game :players count range)]
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (let [player-idxs-and-arrows (->> player-idxs
                                     (map #(get-player-k game % :arrows))
                                     (interleave player-idxs))]
     (-> game
         (do-for-players lose-life player-idxs-and-arrows)
         (do-for-players discard-arrows player-idxs-and-arrows)))))

(defn gatling-gun
  ([game]
   (let [active-player-idx (:active-player-idx game)
         player-idxs (-> game :players count range)]
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [active-player-idx (:active-player-idx game)
         player-idxs-and-hits (interleave player-idxs (repeat 1))]
     (-> game
         (do-for-players lose-life player-idxs-and-hits)
         (discard-arrows (get-player-k game active-player-idx :arrows))))))

(defn setup-dice [game]
  (let [active-player-idx (:active-player-idx game)
        base-dice (repeat (:dice-count cfg/defaults) {:type :base})
        dice (case (get-player-k game active-player-idx :name)
               "JOSE DELGADO" (cons {:type :loudmouth} base-dice)
               "TEQUILA JOE"  (cons {:type :coward} base-dice)
               base-dice)]
    (-> game
        (assoc :dice dice)
        (assoc :dice-rolls 0)
        (#(apply roll-dice % (range (count dice)))))))

(defn end-turn [game]
  (-> game
      (assoc :active-player-idx (next-active-player-idx game))
      (setup-dice)))
