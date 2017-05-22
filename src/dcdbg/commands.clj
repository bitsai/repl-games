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

(defn- to-space-index [game space-*]
  (cond
    (keyword? space-*) (find-space-index game space-*)
    (number? space-*)  space-*
    :else              (throw (Exception. "Invalid space-*!"))))

(defn- get-space [game space-*]
  (let [space-idx (to-space-index game space-*)]
    (-> game :state (nth space-idx))))

(defn- get-cards [game space-*]
  (-> game (get-space space-*) :cards))

(defn- get-card [game space-* card-idx]
  (-> game (get-cards space-*) (nth card-idx)))

(defn- count-cards [game space-*]
  (-> game (get-cards space-*) count))

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

(defn- update-cards [game space-* update-fn]
  (let [space-idx (to-space-index game space-*)]
    (update-in game [:state space-idx :cards] update-fn)))

(defn- move* [game from-* to-* to-top-or-bottom card-idxs]
  (let [card-idxs (or (seq card-idxs) [0])
        from-cards (get-cards game from-*)
        to-facing (-> game (get-space to-*) :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-* #(remove-cards % card-idxs))
        (update-cards to-* #(add-cards % moved to-top-or-bottom)))))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game space-*]
   (let [space-idx (to-space-index game space-*)]
     (-> game
         ;; turn cards face-up so they are all visible
         (update-cards space-* (fn [cards]
                                 (map #(assoc % :facing :up) cards)))
         (get-space space-*)
         (print/print-pile! space-idx))))
  ([game space-* card-idx & card-idxs]
   (print/print-card-details! (get-card game space-* card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game space-* idx)))))

(defn move [game from-* to-* to-top-or-bottom & card-idxs]
  (move* game from-* to-* to-top-or-bottom card-idxs))

(defn refill-line-up [game]
  (move game :main-deck :line-up :top))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-count (count-cards game :deck)
         discard-count (count-cards game :discard)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move* game :deck :hand :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game
           (update-cards :discard rand/shuffle*)
           (move* :discard :deck :bottom (range discard-count))
           (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "Not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (move* game :hand :discard :top (range hand-count))))

(defn flip-super-villain [game]
  (let [[sv & svs] (get-cards game :super-villain)
        new-svs (-> (assoc sv :facing :up) (cons svs))
        msgs (concat (when-let [so (:stack-ongoing sv)]
                       [(format "SUPER-VILLAIN ONGOING: %s" so)])
                     (when-let [faa (:first-appearance-attack sv)]
                       [(format "SUPER-VILLAIN ATTACK: %s" faa)]))]
    (cond-> game
      ;; if top super-villain is face-down, flip it up and show effects
      (-> sv :facing (= :down))
      (-> (update-cards :super-villain (constantly new-svs))
          (update :messages concat msgs)))))

(defn advance-countdown [game]
  (move game :countdown :weakness :top))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (refill-line-up)
       (flip-super-villain)
       (advance-countdown))))
