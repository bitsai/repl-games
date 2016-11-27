(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [active-player-idx idx player]
  (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                   (if (= active-player-idx idx)
                     ">"
                     " ")
                   idx
                   (-> player :role name str/upper-case)
                   (-> player :life)
                   (-> player :max-life)
                   (-> player :arrows)))
  (when (#{:sheriff :deputy} (-> player :role))
    (println (format "     %s: %s"
                     (-> player :name)
                     (-> player :ability)))))

(defn- print-die! [active-die-idxs idx die]
  (println (format "%s[%s] %s"
                   (if (active-die-idxs idx)
                     ">"
                     " ")
                   idx
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
