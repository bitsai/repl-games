(ns dcdbg.cards.location)

(def base
  [{:name "Arkham Asylum"
    :cost 5
    :victory 1
    :ongoing "When you play your first Villain on each of your turns, draw a card."
    :copies 1}
   {:name "The Batcave"
    :cost 5
    :victory 1
    :ongoing "When you play your first Equipment on each of your turns, draw a card."
    :copies 1}
   {:name "Fortress of Solitude"
    :cost 5
    :victory 1
    :ongoing "When you play your first Super Power on each of your turns, draw a card."
    :copies 1}
   {:name "Titans Tower"
    :cost 5
    :victory 1
    :ongoing "When you play your first card with cost 2 or 3 on each of your turns, draw a card."
    :copies 1}
   {:name "The Watchtower"
    :cost 5
    :victory 1
    :ongoing "When you play your first Hero on each of your turns, draw a card."
    :copies 1}])

(def crisis1
  [{:name "House of Mystery"
    :cost 4
    :victory 1
    :ongoing "The first time you play a card with cost 5 or greater during each of your turns, draw a card."
    :copies 1}
   {:name "The Rot"
    :cost 4
    :victory 1
    :ongoing "The first time you play a Weakness or Vulnerability during each of your turns, draw a card."
    :copies 1}])

(def all
  (->> (concat base crisis1)
       (map #(assoc % :type :location))))
