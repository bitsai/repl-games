(ns btdg.print
  (:require [clojure.string :as str]))

(defn- ->active-marker [active-idxs idx]
  (if (or (and (integer? active-idxs)
               (= active-idxs idx))
          (and (set? active-idxs)
               (contains? active-idxs idx)))
    ">"
    " "))

(defn- print-player! [active-player-idx player-idx player]
  (if-not (-> player :life pos?)
    (println (format "%s[%s] DEAD"
                     (->active-marker active-player-idx player-idx)
                     player-idx))
    (let [role (:role player)]
      (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                       (->active-marker active-player-idx player-idx)
                       player-idx
                       (-> role name str/upper-case)
                       (-> player :life)
                       (-> player :max-life)
                       (-> player :arrows)))
      (when (#{:sheriff :deputy} role)
        (println (format "     %s: %s"
                         (-> player :name)
                         (-> player :ability)))))))

(defn- print-die! [active-die-idxs die-idx die]
  (println (format "%s[%s] %s"
                   (->active-marker active-die-idxs die-idx)
                   die-idx
                   die)))

(defn print-game! [game]
  (let [active-player-idx (-> game :state :active-player-idx)
        active-die-idxs (-> game :state :active-die-idxs)]
    (->> game
         (:state)
         (:players)
         (map-indexed (partial print-player! active-player-idx))
         (dorun))
    (println (format " ARROWS %d" (-> game :state :arrows)))
    (println (format " DICE ROLLS %d" (-> game :state :dice-rolls)))
    (->> game
         (:state)
         (:dice)
         (map-indexed (partial print-die! active-die-idxs))
         (dorun))))
