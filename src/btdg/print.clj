(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [idx player]
  (println (format "[%2s] %s (%d/%d life) (%d arrows)"
                   idx
                   (-> player :role name str/upper-case)
                   (-> player :life)
                   (-> player :max-life)
                   (-> player :arrows)))
  (when (#{:sheriff :deputy} (-> player :role))
    (println (format "     %s: %s"
                     (-> player :name)
                     (-> player :ability)))))

(defn print-game! [game]
  (->> game
       (:state)
       (:players)
       (map-indexed print-player!)
       (dorun)))
