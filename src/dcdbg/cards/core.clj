(ns dcdbg.cards.core
  (:require [dcdbg.cards.compiled :as compiled])
  (:import [java.util UUID]))

;; helpers

(defn- mk-cards [card-spec]
  (let [n (:copies card-spec 1)]
    (->> (dissoc card-spec :copies)
         (repeat n)
         (map #(assoc % :id (str (UUID/randomUUID)))))))

(defn- get-cards [sets types]
  (let [sets (set sets)
        types (set types)]
    (->> compiled/cards
         (filter #(and (-> % :set sets) (-> % :type types)))
         (mapcat mk-cards))))

;; cards

(def kick
  (mk-cards {:name "Kick"
             :type :super-power
             :cost 3
             :victory 1
             :power 2
             :copies 16}))

(def punch
  (mk-cards {:name "Punch"
             :type :starter
             :cost 0
             :victory 0
             :power 1
             :copies 36}))

(def vulnerability
  (mk-cards {:name "Vulnerability"
             :type :starter
             :cost 0
             :victory 0
             :copies 16}))

(def weakness
  (mk-cards {:name "Weakness"
             :cost 0
             :victory -1
             :text "Weakness cards reduce your score at the end of the game."
             :copies 20}))

(def main-deck-base (get-cards [:base] [:equipment
                                        :hero
                                        :location
                                        :super-power
                                        :villain]))

(def main-deck-crossover-5 (get-cards [:crossover-5] [:equipment
                                                      :hero
                                                      :location
                                                      :super-power
                                                      :villain]))

(def super-hero (get-cards [:base] [:super-hero]))

(def super-villain (get-cards [:crisis-1] [:super-villain]))
