(ns dcdbg.cards.core)

(def weakness
  {:name "Weakness"
   :cost 0
   :victory -1
   :text "Weakness cards reduce your score at the end of the game."
   :copies 20})

(def kick
  {:name "Kick"
   :type :super-power
   :cost 3
   :victory 1
   :power 2
   :copies 16})

(def vulnerability
  {:name "Vulnerability"
   :type :starter
   :cost 0
   :victory 0
   :copies 16})

(def punch
  {:name "Punch"
   :type :starter
   :cost 0
   :victory 0
   :power 1
   :copies 36})
