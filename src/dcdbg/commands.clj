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

(defn- move* [game from-space-* to-space-* to-top-or-bottom card-idxs]
  (let [from-cards (get-cards game from-space-*)
        to-facing (-> game (get-space to-space-*) :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-space-* #(remove-cards % card-idxs))
        (update-cards to-space-* #(add-cards % moved to-top-or-bottom)))))

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

(defn move [game from-space-* to-space-* to-top-or-bottom & card-idxs]
  (move* game from-space-* to-space-* to-top-or-bottom card-idxs))

(defn gain
  ([game space-*]
   (gain game space-* 1))
  ([game space-* n]
   (move* game space-* :discard :top (range n))))

(defn refill-deck [game]
  (let [discard-count (count-cards game :discard)
        shuffled (update-cards game :discard rand/shuffle*)]
    (move* shuffled :discard :deck :bottom (range discard-count))))

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
       (-> game (refill-deck) (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "Not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (move* game :hand :discard :top (range hand-count))))

(defn exec-super-villain-plan [game]
  (if (-> game (get-cards :line-up) empty?)
    game
    (let [last-idx (dec (count-cards game :line-up))]
      (move game :line-up :destroyed :top last-idx))))

(defn refill-line-up [game]
  (if (>= (count-cards game :line-up) (:line-up-count cfg/defaults))
    game
    (let [card (get-card game :main-deck 0)
          msg (when-let [a (:attack card)]
                [(format "VILLAIN ATTACK: %s" a)])]
      (recur (-> game
                 (move :main-deck :line-up :top 0)
                 (update :messages concat msg))))))

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
  (move game :countdown :weakness :top 0))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (exec-super-villain-plan)
       (refill-line-up)
       ;; exec-villains-plan
       (flip-super-villain)
       (advance-countdown))))
