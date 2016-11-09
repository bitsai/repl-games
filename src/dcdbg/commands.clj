(ns dcdbg.commands
  (:require [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

;; helpers

(defn- find-space-index [game space-name]
  (->> game
       (:state)
       (keep-indexed (fn [idx space]
                       (when (-> space :name (= space-name))
                         idx)))
       (first)))

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

(defn- update-cards [game space-idx update-fn]
  (update-in game [:state space-idx :cards] update-fn))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game space-idx]
   (-> game
       (get-cards space-idx)
       ;; turn everything face-up so they are all visible
       (->> (map #(assoc % :facing :up)))
       (print/print-pile! space-idx (get-space-name game space-idx))))
  ([game k card-idx & card-idxs]
   (print/print-card-details! (get-card game k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game k idx)))))

(defn move [game from-space-idx to-space-idx to-top-or-bottom & card-idxs]
  (let [from-cards (get-cards game from-space-idx)
        to-space-name (get-space-name game to-space-idx)
        to-facing (-> cfg/card-spaces to-space-name :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))
        concat-fn #(case to-top-or-bottom
                     :top    (concat moved %)
                     :bottom (concat % moved))]
    (-> game
        (update-cards from-space-idx #(remove-cards % card-idxs))
        (update-cards to-space-idx concat-fn))))

(defn gain
  ([game space-idx]
   (gain game space-idx 1))
  ([game space-idx n]
   (let [discard-idx (find-space-index game :discard)]
     (apply move game space-idx discard-idx :top (range n)))))

(defn refill-deck [game]
  (let [deck-idx (find-space-index game :deck)
        discard-idx (find-space-index game :discard)
        n (-> game (get-cards discard-idx) count)
        shuffled (update-cards game discard-idx rand/shuffle*)]
    (apply move shuffled discard-idx deck-idx :bottom (range n))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [hand-idx (find-space-index game :hand)
         deck-idx (find-space-index game :deck)]
     (apply move game deck-idx hand-idx :bottom (range n)))))
