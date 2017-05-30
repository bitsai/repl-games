(ns dcdbg.cards.core
  (:require [dcdbg.cards.compiled :as compiled]))

(def kick
  {:name "Kick"
   :type :super-power
   :cost 3
   :victory 1
   :power 2
   :copies 16})

(def punch
  {:name "Punch"
   :type :starter
   :cost 0
   :victory 0
   :power 1
   :copies 36})

(def vulnerability
  {:name "Vulnerability"
   :type :starter
   :cost 0
   :victory 0
   :copies 16})

(def weakness
  {:name "Weakness"
   :cost 0
   :victory -1
   :text "Weakness cards reduce your score at the end of the game."
   :copies 20})

(defn- get-cards [type & sets]
  (let [sets (set sets)
        cards (filter #(-> % :type (= type)) compiled/cards)]
    (if-not set
      cards
      (filter #(-> % :set sets) cards))))

(def equipment (get-cards :equipment))

(def hero (get-cards :hero))

(def location (get-cards :location))

(def super-hero (get-cards :super-hero :base :crisis-1))

(def super-power (get-cards :super-power))

(def super-villain (get-cards :super-villain :crisis-1))

(def villain (get-cards :villain))
