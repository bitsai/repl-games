(ns dcdbg.cards
  (:require [dcdbg.cards.equipment :as equipment]
            [dcdbg.cards.hero :as hero]
            [dcdbg.cards.location :as location]
            [dcdbg.cards.super-hero :as super-hero]
            [dcdbg.cards.super-power :as super-power]
            [dcdbg.cards.super-villain :as super-villain]
            [dcdbg.cards.villain :as villain]))

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

(def equipment equipment/all)

(def hero hero/all)

(def location location/all)

(def super-hero super-hero/all)

(def super-power super-power/all)

(def super-villain super-villain/all)

(def villain villain/all)
