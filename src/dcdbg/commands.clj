(ns dcdbg.commands
  (:require [dcdbg.print :as print]))

;; helpers

(defn- get-cards [game k]
  (-> game :state k))

(defn- get-card [game k card-idx]
  (let [;; convert 1-based index to 0-based index
        idx (dec card-idx)]
    (-> game (get-cards k) (nth idx))))

;; commands

(defn print*
  ([game]
   (print/print-game game))
  ([game k]
   (let [;; turn everything face-up so they are all visible
         cards (-> game
                   (get-cards k)
                   (->> (map #(assoc % :facing :up))))]
     (print/print-pile cards k)))
  ([game k card-idx & card-idxs]
   (print/print-card-details (get-card game k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details (get-card game k idx)))))
