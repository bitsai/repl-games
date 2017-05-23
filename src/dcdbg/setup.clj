(ns dcdbg.setup
  (:require [dcdbg.cards.core :as cards]
            [dcdbg.cards.equipment :as equipment]
            [dcdbg.cards.hero :as hero]
            [dcdbg.cards.location :as location]
            [dcdbg.cards.super-hero :as super-hero]
            [dcdbg.cards.super-power :as super-power]
            [dcdbg.cards.super-villain :as super-villain]
            [dcdbg.cards.villain :as villain]
            [dcdbg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- mk-cards [card-spec]
  (let [n (:copies card-spec)]
    (->> (dissoc card-spec :copies) (repeat n))))

(defn- flip [cards facing]
  (mapv #(assoc % :facing facing) cards))

;; setup

(defn- setup-deck []
  (->> [[cards/punch (-> cfg/defaults :punch-count)]
        [cards/vulnerability (-> cfg/defaults :vulnerability-count)]]
       (mapcat (fn [[card-spec n]]
                 (->> card-spec (mk-cards) (take n))))
       (rand/shuffle*)))

(defn- setup-main-deck []
  (->> (concat equipment/all
               hero/all
               location/all
               super-power/all
               villain/all)
       (mapcat mk-cards)
       ;; use only 1 copy of each card
       (distinct)
       (rand/shuffle*)))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] super-hero/all]
    [x (rand/rand-nth* xs)]))

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x y & zs] super-villain/all
        zs (->> zs (rand/shuffle*) (take (- n 2)))]
    ;; set Ra's Al-Ghul on top, Crisis Anti-Monitor on bottom
    (concat [x] zs [y])))

(defn mk-game-state [game]
  (let [{:keys [hand-size line-up-size super-villain-count]} cfg/defaults
        svs (setup-super-villains super-villain-count)
        [line-up main-deck] (split-at line-up-size (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at hand-size (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "SUPER-HERO [%s] %s" (:name %) (:text %))))
                   (let [sv (first svs)]
                     (format "ONGOING [%s] %s" (:name sv) (:ongoing sv))))
        state (for [[name cards] [[:super-villain svs]
                                  [:countdown (mk-cards cards/weakness)]
                                  [:weakness []]
                                  [:kick (mk-cards cards/kick)]
                                  [:destroyed []]
                                  [:main-deck main-deck]
                                  [:line-up line-up]
                                  [:super-hero shs]
                                  [:location []]
                                  [:hand hand]
                                  [:deck deck]
                                  [:discard []]]
                    :let [{:keys [facing type]} (get cfg/zones name)]]
                {:name name
                 :type type
                 :facing facing
                 :cards (flip cards facing)})]
    (-> game
        (assoc :messages msgs)
        (assoc :state (vec state))
        ;; flip first super-villain face up
        (assoc-in [:state 0 :cards 0 :facing] :up))))
