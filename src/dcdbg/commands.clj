(ns dcdbg.commands
  (:require [dcdbg.config :as cfg]
            [dcdbg.print :as print]))

;; helpers

(defn- get-cards [game space-idx]
  (-> game :state (nth space-idx) :cards))

(defn- get-card [game space-idx card-idx]
  (-> game (get-cards space-idx) (nth card-idx)))

(defn- get-space-name [game space-idx]
  (-> game :state (nth space-idx) :name))

(defn- remove-cards [cards card-idxs]
  (let [card-idx-set (set card-idxs)]
    (keep-indexed (fn [idx card]
                    (when-not (card-idx-set idx)
                      card))
                  cards)))

;; commands

(defn print*
  ([game]
   (print/print-game game))
  ([game space-idx]
   (-> game
       (get-cards space-idx)
       ;; turn everything face-up so they are all visible
       (->> (map #(assoc % :facing :up)))
       (print/print-pile space-idx (get-space-name game space-idx))))
  ([game k card-idx & card-idxs]
   (print/print-card-details (get-card game k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details (get-card game k idx)))))

(defn move [game from-space-idx to-space-idx to-top-or-bottom & card-idxs]
  (let [from-cards (get-cards game from-space-idx)
        to-space-name (get-space-name game to-space-idx)
        to-facing (-> cfg/card-spaces to-space-name :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))
        update-fn #(case to-top-or-bottom
                     :top    (concat moved %)
                     :bottom (concat % moved))]
    (-> game
        (update-in [:state from-space-idx :cards] remove-cards card-idxs)
        (update-in [:state to-space-idx :cards] update-fn))))
