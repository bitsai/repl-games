(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [idx player]
  (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                   (if (-> player :active?)
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

(defn print-game! [game]
  (->> game
       (:state)
       (:players)
       (map-indexed print-player!)
       (dorun))
  (println (format " ARROWS %d" (-> game :state :arrows))))
