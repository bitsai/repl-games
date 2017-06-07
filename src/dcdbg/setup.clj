(ns dcdbg.setup
  (:require [dcdbg.cards.core :as cards]
            [dcdbg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- flip [cards facing]
  (mapv #(assoc % :facing facing) cards))

;; setup

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x & svs] cards/super-villain
        ys (->> svs butlast rand/shuffle* (take (- n 2)))
        z (last svs)]
    ;; set Ra's Al-Ghul on top, Crisis Anti-Monitor on bottom
    (concat [x] ys [z])))

(defn- setup-main-deck []
  (let [n (-> cards/main-deck-base count (/ 2))
        [xs ys] (->> cards/main-deck-base rand/shuffle* (split-at n))]
    (-> cards/main-deck-crossover-5
        (concat xs)
        (rand/shuffle*)
        (concat ys))))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] cards/super-hero]
    [x (-> xs rand/shuffle* first)]))

(defn- setup-deck []
  (-> (take (:vulnerability-count cfg/defaults) cards/vulnerability)
      (concat (take (:punch-count cfg/defaults) cards/punch))
      (rand/shuffle*)))

(defn mk-game-state [game]
  (let [{:keys [hand-size line-up-size super-villain-count]} cfg/defaults
        svs (setup-super-villains super-villain-count)
        [line-up main-deck] (split-at line-up-size (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at hand-size (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "TEXT (%s): %s" (:name %) (:text %))))
                   (let [sv (first svs)]
                     (format "ONGOING (%s): %s" (:name sv) (:ongoing sv))))
        zones (for [[name cards] [[:super-villain svs]
                                  [:weakness cards/weakness]
                                  [:timer []]
                                  [:kick cards/kick]
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
        (assoc :zones (vec zones))
        ;; flip first super-villain face up
        (assoc-in [:zones 0 :cards 0 :facing] :up)
        ;; store :id of last Line-Up card
        (assoc :last-line-up-id (-> zones (nth 6) :cards last :id)))))
