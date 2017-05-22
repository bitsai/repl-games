(ns dcdbg.cards.equipment)

(def base
  [{:name "Aquaman's Trident"
    :cost 3
    :victory 1
    :power 2
    :text "You may put any one card you buy or gain this turn on top of your deck."
    :copies 3}
   {:name "Batmobile"
    :cost 2
    :victory 1
    :power "1?"
    :text "If this is the first card you play this turn, discard your hand and draw five cards. Otherwise, +1 Power."
    :copies 3}
   {:name "The Bat-Signal"
    :cost 5
    :victory 1
    :power 1
    :text "Put a Hero from your discard pile into your hand."
    :copies 3}
   {:name "The Cape and Cowl"
    :cost 4
    :victory 1
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :copies 3}
   {:name "Green Arrow's Bow"
    :cost 4
    :victory 1
    :power 2
    :text "Super-Villains cost you 2 less to defeat this turn."
    :copies 3}
   {:name "Lasso of Truth"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card."
    :power 1
    :copies 3}
   {:name "Nth Metal"
    :cost 3
    :victory 1
    :power 1
    :text "Look at the top card of your deck. You may destroy it."
    :copies 4}
   {:name "Power Ring"
    :cost 3
    :victory 1
    :power "2+"
    :text "Reveal the top card of your deck. If its cost is 1 or greater, +3 Power. Otherwise, +2 Power."
    :copies 3}
   {:name "Utility Belt"
    :cost 5
    :victory "5?"
    :power 2
    :text "At the end of the game, if you have four or more other Equipment in your deck, this card is worth 5 VPs."
    :copies 3}])

(def crisis1
  [{:name "Bo Staff"
    :cost 3
    :victory 1
    :power "2+"
    :text "+3 Power if there are one or more Villains in the Line-Up. Otherwise, +2 Power."
    :copies 2}
   {:name "Magician's Corset"
    :cost 5
    :victory 1
    :power "0+"
    :text "Choose any number of players. Each of those players draws a card. +1 Power for each player who draws a card this way (including you)."
    :copies 1}
   {:name "Quiver of Arrows"
    :cost 1
    :victory 1
    :text "Choose any number of players. Each of those players reveals the top card of his deck and may discard it."
    :copies 2}
   {:name "Signature Trenchcoat"
    :cost 4
    :victory 1
    :power "2?"
    :text "You may gain a card with cost 5 or less from the destroyed pile. If you choose not to, +2 Power."
    :copies 2}])

(def forever-evil
  [{:name "Broadsword"
    :cost 6
    :victory 2
    :attack "Destroy a card with cost 1, 2, or 3 in a foe's discard pile."
    :power 2
    :copies 2}
   {:name "Cold Gun"
    :cost 2
    :victory 1
    :power 1
    :text "You may put a Frozen token on a card in the Line-Up. If you do, remove it at the start of your next turn."
    :copies 3}
   {:name "Cosmic Staff"
    :cost 5
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, gain the bottom card of the main deck."
    :power 2
    :copies 3}
   {:name "Firestorm Matrix"
    :cost 7
    :victory 2
    :text "Play the top card of your deck. If its cost is 5 or less, you may destroy this card. If you do, leave the card you played in front of you for the rest of the game and it has \"Ongoing: You may play this card once during each of your turns. (At end of game, destroy it.)\""
    :copies 2}
   {:name "Mallet"
    :cost 4
    :victory 1
    :text "Reveal the top card of your deck. Draw it or pass it to any player's discard pile."
    :copies 3}
   {:name "Man-Bat Serum"
    :cost 3
    :victory 1
    :power "0+"
    :text "+Power equal to your VPs. If you have 5 or more VPs, destroy this card at the end of your turn."
    :copies 3}
   {:name "Pandora's Box"
    :cost 2
    :victory 1
    :text "Reveal the top card of the main deck. Add cards from the main deck to the Line-Up equal to the revealed card's cost."
    :copies 2}
   {:name "Power Armor"
    :cost 8
    :victory 3
    :defense "You may reveal this card from your hand to avoid an Attack. If you do, you may destroy a card in your hand or discard pile."
    :power 3
    :copies 1}
   {:name "Secret Society Communicator"
    :cost 4
    :victory 1
    :power "2+"
    :text "You may destroy a card in your hand or discard pile. If it's a Hero, +Power equal to its Cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Sledgehammer"
    :cost 3
    :victory 1
    :text "You may destroy a non-Equipment card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "Venom Injector"
    :cost 1
    :victory 0
    :power "2?"
    :text "When you destroy this card in any zone, +2 Power."
    :copies 2}])

(def all
  (->> (concat base crisis1 forever-evil)
       (map #(assoc % :type :equipment))))
