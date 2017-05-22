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

(def forever-evil
  [{:name "Belle Reve"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Villain you play"
    :copies 1}
   {:name "Blackgate Prison"
    :cost 4
    :victory 1
    :ongoing "Once during each of your turns, reveal the top card of your deck. If it's a Vulnerability or Weakness, destroy it and gain 1 VP."
    :copies 1}
   {:name "Central City"
    :cost 4
    :victory 1
    :ongoing "+1 Power for each non-Kick Super Power you play."
    :copies 1}
   {:name "Earth-3"
    :cost 6
    :victory 1
    :ongoing "Once during each of your turns, reveal the top card of your deck. If it's a Punch, destroy it and gain 1 VP."
    :copies 1}
   {:name "Happy Harbor"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Hero you play."
    :copies 1}
   {:name "S.T.A.R. Labs"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Equipment you play."
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
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :location))))
