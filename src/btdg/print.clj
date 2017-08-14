(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [active-player-idx player-idx player]
  (let [active-marker (if (= active-player-idx player-idx) "<" " ")]
    (if (-> player :life pos?)
      (let [role-name (-> player :role name str/upper-case)
            {:keys [name role max-life life arrows]} player]
        (println (format "(%s)%s %-8s %d/%d %d %s"
                         player-idx
                         active-marker
                         role-name
                         life
                         max-life
                         arrows
                         (or name "")))
        (when-let [ability (:ability player)]
          (println (format "     %s" ability))))
      (println (format "(%s)%s DEAD" player-idx active-marker)))))

(defn- print-die! [die-idx {:keys [new? value]}]
  (let [new-marker (if new? "<" " ")]
    (println (format "(%s)%s %s" die-idx new-marker value))))

(defn print-game! [game]
  (->> game
       (:players)
       (map-indexed (partial print-player! (:active-player-idx game)))
       (dorun))
  (println (format "ARROWS %d" (:arrows game)))
  (println (format "DICE ROLLS %d" (:dice-rolls game)))
  (->> game
       (:dice)
       (map-indexed print-die!)
       (dorun)))
