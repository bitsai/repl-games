(ns dcdbg.cards.equipment)

(def base
  [{:name "Aquaman's Trident"
    :cost 3
    :victory 1
    :text "You may put any one card you buy or gain this turn on top of your deck."
    :power 2
    :defense nil
    :copies 3}
   {:name "Batmobile"
    :cost 2
    :victory 1
    :text "If this is the first card you play this turn, discard your hand and draw five cards. Otherwise, +1 Power."
    :power "1?"
    :defense nil
    :copies 3}
   {:name "The Bat-Signal"
    :cost 5
    :victory 1
    :text "Put a Hero from your discard pile into your hand."
    :power 1
    :defense nil
    :copies 3}
   {:name "The Cape and Cowl"
    :cost 4
    :victory 1
    :text nil
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :copies 3}
   {:name "Green Arrow's Bow"
    :cost 4
    :victory 1
    :text "Super-Villains cost you 2 less to defeat this turn."
    :power 2
    :defense nil
    :copies 3}
   {:name "Lasso of Truth"
    :cost 2
    :victory 1
    :text nil
    :power 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card."
    :copies 3}
   {:name "Nth Metal"
    :cost 3
    :victory 1
    :text "Look at the top card of your deck. You may destroy it."
    :power 1
    :defense nil
    :copies 4}
   {:name "Power Ring"
    :cost 3
    :victory 1
    :text "Reveal the top card of your deck. If its cost is 1 or greater, +3 Power. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 3}
   {:name "Utility Belt"
    :cost 5
    :victory "5?"
    :text "At the end of the game, if you have four or more other Equipment in your deck, this card is worth 5 VPs."
    :power 2
    :defense nil
    :copies 3}])

(def crisis1
  [{:name "Bo Staff"
    :cost 3
    :victory 1
    :text "+3 Power if there are one or more Villains in the Line-Up. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 2}
   {:name "Magician's Corset"
    :cost 5
    :victory 1
    :text "Choose any number of players. Each of those players draws a card. +1 Power for each player who draws a card this way (including you)."
    :power "0+"
    :defense nil
    :copies 1}
   {:name "Quiver of Arrows"
    :cost 1
    :victory 1
    :text "Choose any number of players. Each of those players reveals the top card of his deck and may discard it."
    :power nil
    :defense nil
    :copies 2}
   {:name "Signature Trenchcoat"
    :cost 4
    :victory 1
    :text "You may gain a card with cost 5 or less from the destroyed pile. If you choose not to, +2 Power."
    :power "2?"
    :defense nil
    :copies 2}])

(def all
  (->> (concat base crisis1)
       (map #(assoc % :type :equipment))))
