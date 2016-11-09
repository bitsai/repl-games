(ns dcdbg.commands
  (:require [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

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
   (apply move game space-idx 11 :top (range 1 (inc n)))))

(defn refill-deck [game]
  (let [discard-count (-> game (get-cards 11) count)
        shuffled (update-cards game 11 rand/shuffle*)]
    (apply move shuffled 11 10 :bottom (range discard-count))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (cond
     ;; if player deck has enough cards, draw
     (-> game (get-cards 10) count (>= n))
     (apply move game 10 9 :bottom (range n))

     ;; if player deck has too few cards but there are discards, refill then draw
     (-> game (get-cards 11) seq)
     (-> game refill-deck (draw n))

     :else
     (throw (Exception. "not enough cards!")))))
