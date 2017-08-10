(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [active-player-idx player-idx player]
  (let [active-marker (if (= active-player-idx player-idx) ">" " ")]
    (if (-> player :life pos?)
      (let [role (:role player)]
        (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                         active-marker
                         player-idx
                         (-> role name str/upper-case)
                         (-> player :life)
                         (-> player :max-life)
                         (-> player :arrows)))
        (when (#{:sheriff :deputy} role)
          (println (format "     %s: %s"
                           (-> player :name)
                           (-> player :ability)))))
      (println (format "%s[%s] DEAD" active-marker player-idx)))))

(defn- print-die! [die-idx {:keys [new? value]}]
  (let [new-roll-marker (if new? ">" " ")]
    (println (format "%s[%s] %s" new-roll-marker die-idx value))))

(defn print-game! [game]
  (->> game
       (:players)
       (map-indexed (partial print-player! (:active-player-idx game)))
       (dorun))
  (println (format " ARROWS %d" (:arrows game)))
  (println (format " DICE ROLLS %d" (:dice-rolls game)))
  (->> game
       (:dice)
       (map-indexed print-die!)
       (dorun)))
