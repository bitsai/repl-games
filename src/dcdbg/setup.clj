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
  (let [n (:copies card-spec 1)]
    (->> (dissoc card-spec :copies)
         (repeat n))))

(defn- flip [cards facing]
  (map #(assoc % :facing facing) cards))

(defn- use-facing [cards k]
  (let [facing (-> cfg/card-spaces k :facing)]
    (flip cards facing)))

;; setup

(defn- setup-deck []
  (->> [[cards/punch (-> cfg/defaults :punch-count)]
        [cards/vulnerability (-> cfg/defaults :vulnerability-count)]]
       (mapcat (fn [[card-spec n]]
                 (->> card-spec
                      (mk-cards)
                      (take n))))
       (rand/shuffle*)))

(defn- setup-main-deck []
  (->> (concat equipment/all
               hero/all
               location/all
               super-power/all
               villain/all)
       (mapcat mk-cards)
       (rand/shuffle*)))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] super-hero/all]
    (-> [x (rand/rand-nth* xs)]
        (use-facing :super-hero))))

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x y & z] super-villain/all
        z (->> z
               (rand/shuffle*)
               (take (- n 2)))]
    ;; flip Ra's Al-Ghul up, set Crisis Anti-Monitor on bottom
    (concat (-> [x] (flip :up))
            (->  z  (use-facing :super-villain))
            (-> [y] (use-facing :super-villain)))))

(defn mk-game-state [game]
  (let [svs (setup-super-villains (:super-villain-count cfg/defaults))
        [line-up main-deck] (split-at 5 (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at 5 (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "SUPER-HERO: %s" (:text %))))
                   (->> svs
                        (first)
                        (:stack-ongoing)
                        (format "SUPER-VILLAIN ONGOING: %s")))
        state [{:name :super-villain
                :cards svs}
               {:name :timer
                :cards (-> cards/weakness mk-cards (use-facing :weakness))}
               {:name :weakness
                :cards []}
               {:name :kick
                :cards (-> cards/kick mk-cards (use-facing :kick))}
               {:name :destroyed
                :cards []}
               {:name :main-deck
                :cards (-> main-deck (use-facing :main-deck))}
               {:name :line-up
                :cards (-> line-up (use-facing :line-up))}
               {:name :super-hero
                :cards shs}
               {:name :location
                :cards []}
               {:name :hand
                :cards (-> hand (use-facing :hand))}
               {:name :deck
                :cards (-> deck (use-facing :deck))}
               {:name :discard
                :cards []}]]
    (-> game
        (assoc :messages msgs)
        (assoc :state state))))
