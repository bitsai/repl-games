(ns dcdbg.commands
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

;; helpers

(defn- find-zone-index [game zone-name]
  (->> game
       (:zones)
       (keep-indexed (fn [idx zone]
                       (when (-> zone :name (= zone-name))
                         idx)))
       (first)))

(defn- to-zone-index [game zone-*]
  (cond
    (keyword? zone-*) (find-zone-index game zone-*)
    (number? zone-*)  zone-*
    :else              (throw (Exception. "Invalid zone-*!"))))

(defn- get-zone [game zone-*]
  (let [zone-idx (to-zone-index game zone-*)]
    (-> game :zones (nth zone-idx))))

(defn- get-cards [game zone-*]
  (-> game (get-zone zone-*) :cards))

(defn- get-card [game zone-* card-idx]
  (-> game (get-cards zone-*) (nth card-idx)))

(defn- count-cards [game zone-*]
  (-> game (get-cards zone-*) count))

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

(defn- update-cards [game zone-* update-fn]
  (let [zone-idx (to-zone-index game zone-*)]
    (update-in game [:zones zone-idx :cards] update-fn)))

(defn- move* [game from-* to-* top-or-bottom & card-idxs]
  (let [card-idxs (or (seq card-idxs) [0])
        from-cards (get-cards game from-*)
        to-facing (-> game (get-zone to-*) :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-* #(remove-cards % card-idxs))
        (update-cards to-* #(add-cards % moved top-or-bottom)))))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game zone-*]
   (let [zone-idx (to-zone-index game zone-*)]
     (-> game
         ;; turn cards face-up so they are all visible
         (update-cards zone-* (fn [cards]
                                 (map #(assoc % :facing :up) cards)))
         (get-zone zone-*)
         (print/print-pile! zone-idx))))
  ([game zone-* card-idx & card-idxs]
   (print/print-card-details! (get-card game zone-* card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game zone-* idx)))))

(defn move [game from-* to-* top-or-bottom card-idxs]
  (apply move* game from-* to-* top-or-bottom card-idxs))

(defn gain
  ([game zone-*]
   (gain game zone-* 1))
  ([game zone-* n]
   (move game zone-* :discard :top (range n))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-count (count-cards game :deck)
         discard-count (count-cards game :discard)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move game :deck :hand :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game
           (update-cards :discard rand/shuffle*)
           (move :discard :deck :bottom (range discard-count))
           (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "Not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (move game :hand :discard :top (range hand-count))))

(defn exec-super-villain-plan [game]
  (let [line-up-count (count-cards game :line-up)]
    (if (< line-up-count (:line-up-size cfg/defaults))
      game
      (move* game :line-up :destroyed :top (dec line-up-count)))))

(defn advance-timer [game]
  (move* game :weakness :timer :top 0))

(defn refill-line-up [game]
  (if (-> game (count-cards :line-up) (>= (:line-up-size cfg/defaults)))
    game
    (let [{:keys [name type attack]} (get-card game :main-deck 0)
          msgs (when (and (#{:hero :villain} type) attack)
                 [(format "ATTACK (%s): %s" name attack)])]
      (-> game
          (update :messages concat msgs)
          (move* :main-deck :line-up :top 0)
          (refill-line-up)))))

(defn flip-super-villain [game]
  (if (-> game (get-card :super-villain 0) :facing (= :up))
    game
    (let [[sv & svs] (get-cards game :super-villain)
          {:keys [name attack ongoing]} sv
          msgs (concat (when attack
                         [(format "ATTACK (%s): %s" name attack)])
                       (when ongoing
                         [(format "ONGOING (%s): %s" name ongoing)]))
          new-svs (-> sv (assoc :facing :up) (cons svs))]
      (-> game
          (update :messages concat msgs)
          (update-cards :super-villain (constantly new-svs))))))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (exec-super-villain-plan)
       (advance-timer)
       (draw n)
       (refill-line-up)
       (flip-super-villain))))
