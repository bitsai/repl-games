(ns dcdbg.commands
  (:require [dcdbg.print :as print]))

;; helpers

(defn- get-card [world-state k card-idx]
  (let [;; convert 1-based index to 0-based index
        idx (dec card-idx)]
    (-> world-state :game-state (get k) (nth idx))))

;; commands

(defn print*
  ([world-state]
   (print/print-world-state world-state))
  ([world-state k]
   (let [;; turn everything face-up so they are all visible
         cards (-> world-state
                   (:game-state)
                   (get k)
                   (->> (map #(assoc % :facing :up))))]
     (print/print-pile cards k)))
  ([world-state k card-idx & card-idxs]
   (print/print-card-details (get-card world-state k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details (get-card world-state k idx)))))
