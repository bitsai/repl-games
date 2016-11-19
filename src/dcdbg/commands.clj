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

(defn- count-cards [game space-idx]
  (-> game (get-cards space-idx) count))

(defn- add-cards [cards new-cards top-or-bottom]
  (case top-or-bottom
    :top (concat new-cards cards)
    :bottom (concat cards new-cards)))

(defn- remove-cards [cards card-idxs]
  (let [card-idx-set (set card-idxs)]
    (keep-indexed (fn [idx card]
                    (when-not (card-idx-set idx)
                      card))
                  cards)))

(defn- update-cards [game space-idx update-fn]
  (update-in game [:state space-idx :cards] update-fn))

(defn- move* [game from-space-idx to-space-idx to-top-or-bottom card-idxs]
  (let [from-cards (get-cards game from-space-idx)
        to-space-name (get-space-name game to-space-idx)
        to-facing (-> cfg/card-spaces to-space-name :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-space-idx #(remove-cards % card-idxs))
        (update-cards to-space-idx #(add-cards % moved to-top-or-bottom)))))

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
  (move* game from-space-idx to-space-idx to-top-or-bottom card-idxs))

(defn gain
  ([game space-idx]
   (gain game space-idx 1))
  ([game space-idx n]
   (let [discard-idx (find-space-index game :discard)]
     (move* game space-idx discard-idx :top (range n)))))

(defn refill-deck [game]
  (let [deck-idx (find-space-index game :deck)
        discard-idx (find-space-index game :discard)
        discard-count (count-cards game discard-idx)
        shuffled (update-cards game discard-idx rand/shuffle*)]
    (move* shuffled discard-idx deck-idx :bottom (range discard-count))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-idx (find-space-index game :deck)
         deck-count (count-cards game deck-idx)
         discard-idx (find-space-index game :discard)
         discard-count (count-cards game discard-idx)
         hand-idx (find-space-index game :hand)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move* game deck-idx hand-idx :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game
           (refill-deck)
           (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [discard-idx (find-space-index game :discard)
        hand-idx (find-space-index game :hand)
        hand-count (count-cards game hand-idx)]
    (move* game hand-idx discard-idx :top (range hand-count))))

(defn refill-line-up [game]
  (let [main-deck-idx (find-space-index game :main-deck)
        line-up-idx (find-space-index game :line-up)
        card (get-card game main-deck-idx 0)
        msg (when-let [a (:attack card)]
              [(format "VILLAIN ATTACK: %s" a)])]
    (-> game
        (move main-deck-idx line-up-idx :top 0)
        (update :messages concat msg))))

(defn flip-super-villain [game]
  (let [super-villain-idx (find-space-index game :super-villain)
        [sv & svs] (get-cards game super-villain-idx)
        flipped (assoc sv :facing :up)
        msgs (concat (when-let [so (:stack-ongoing sv)]
                       [(format "SUPER-VILLAIN ONGOING: %s" so)])
                     (when-let [faa (:first-appearance-attack sv)]
                       [(format "SUPER-VILLAIN ATTACK: %s" faa)]))]
    (cond-> game
      ;; if top super-villain is face-down, flip it up and show effects
      (-> sv :facing (= :down))
      (-> (update-cards super-villain-idx (constantly (cons flipped svs)))
          (update :messages concat msgs)))))

(defn advance-timer [game]
  (let [timer-idx (find-space-index game :timer)
        weakness-idx (find-space-index game :weakness)]
    (move game timer-idx weakness-idx :top 0)))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (refill-line-up)
       (flip-super-villain)
       (advance-timer))))
