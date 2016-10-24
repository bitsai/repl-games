(ns dcdbg.commands
  (:require [dcdbg.print :as print]))

;; helpers

(defn- get-cards [game space-idx]
  (-> game :state (nth space-idx) :cards))

(defn- get-card [game space-idx card-idx]
  (-> game (get-cards space-idx) (nth card-idx)))

;; commands

(defn print*
  ([game]
   (print/print-game game))
  ([game space-idx]
   (let [space-name (-> game :state (nth space-idx) :name)]
     (-> game
         (get-cards space-idx)
         ;; turn everything face-up so they are all visible
         (->> (map #(assoc % :facing :up)))
         (print/print-pile space-idx space-name))))
  ([game k card-idx & card-idxs]
   (print/print-card-details (get-card game k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details (get-card game k idx)))))
