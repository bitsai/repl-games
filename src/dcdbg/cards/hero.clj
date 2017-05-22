(ns dcdbg.cards.hero)

(def base
  [{:name "Blue Beetle"
    :cost 6
    :victory 2
    :defense "You may reveal this card from your hand to avoid an Attack. (It stays in your hand.)"
    :power 3
    :copies 1}
   {:name "Catwoman"
    :cost 2
    :victory 1
    :power 2
    :copies 3}
   {:name "Dark Knight"
    :cost 5
    :victory 1
    :power 2
    :text "Gain all Equipment in the Line-Up. Then, if you play or have played Catwoman this turn, you may put a card you bought or gained this turn into your hand."
    :copies 1}
   {:name "Emerald Knight"
    :cost 5
    :victory 1
    :text "Remove an Equipment, Hero, or Super Power from the Line-Up. Play it, then return it to the Line-Up at the end of your turn."
    :copies 1}
   {:name "The Fastest Man Alive"
    :cost 5
    :victory 1
    :text "Draw two cards."
    :copies 1}
   {:name "Green Arrow"
    :cost 5
    :victory "5?"
    :power 2
    :text "At the end of the game, if you have four or more other Heroes in your deck, this card is worth 5 VPs."
    :copies 3}
   {:name "High-Tech Hero"
    :cost 3
    :victory 1
    :power "1+"
    :text "If you play or have played a Super Power or Equipment this turn, +3 Power. Otherwise, +1 Power."
    :copies 3}
   {:name "J'onn J'onnz"
    :cost 6
    :victory 2
    :text "Play the top card of the Super-Villain stack, then return it to the top of the stack. (The First Appearance -- Attack does not happen.)"
    :copies 1}
   {:name "Kid Flash"
    :cost 2
    :victory 1
    :text "Draw a card."
    :copies 4}
   {:name "King of Atlantis"
    :cost 5
    :victory 1
    :power "1+"
    :text "You may destroy a card in your discard pile. If you do, +3 Power. Otherwise, +1 Power."
    :copies 1}
   {:name "Man of Steel"
    :cost 8
    :victory 3
    :power 3
    :text "Put all Super Powers from your discard pile into your hand."
    :copies 1}
   {:name "Mera"
    :cost 3
    :victory 1
    :power "2+"
    :text "If your discard pile is empty, +4 Power. Otherwise, +2 Power."
    :copies 2}
   {:name "Princess Diana of Themyscira"
    :cost 7
    :victory 2
    :text "Gain all Villains with cost 7 or less in the Line-Up."
    :copies 1}
   {:name "Robin"
    :cost 3
    :victory 1
    :power 1
    :text "Put an Equipment from your discard pile into your hand."
    :copies 3}
   {:name "Supergirl"
    :cost 4
    :victory 1
    :text "You may put a Kick from the Kick stack into your hand."
    :copies 2}
   {:name "Swamp Thing"
    :cost 4
    :victory 1
    :power "2+"
    :text "If you control a Location, +5 Power. Otherwise, +2 Power."
    :copies 2}
   {:name "Zatanna Zatara"
    :cost 4
    :victory 1
    :power 1
    :text "You may put up to two cards from your discard pile on the bottom of your deck."
    :copies 2}])

(def forever-evil
  [{:name "Amanda Waller"
    :cost 4
    :victory 1
    :power "2?"
    :text "You may destroy a card in your hand or discard pile. If it's a Villain, +Power equal to its cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Catwoman"
    :cost 3
    :victory 1
    :attack "Steal 1 VP from a foe."
    :power 1
    :copies 2}
   {:name "Commissioner Gordon"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, gain 1 VP."
    :power 1
    :copies 3}
   {:name "Dr. Light"
    :cost 3
    :victory 1
    :attack "A foe puts a Location he controls into his discard pile."
    :text "Draw a card."
    :copies 2}
   {:name "Element Woman"
    :cost 4
    :victory 1
    :power 2
    :text "While you own or are playing this card, it is also a Super Power, Equipment, and Villain."
    :copies 2}
   {:name "Firestorm"
    :cost 6
    :victory 2
    :text "Put the top card of your deck on your Super-Villain. This card has the game text of each card on your Super-Villain this turn. (At end of game, destroy those cards.)"
    :copies 1}
   {:name "Pandora"
    :cost 7
    :victory 2
    :power "1+"
    :text "Add the top card of the Main Deck to the Line-Up. +1 Power for each different cost among cards in the Line-Up."
    :copies 1}
   {:name "Phantom Stranger"
    :cost 5
    :victory "10*"
    :text "You may destroy a card in your hand and you may destroy a card in your discard pile. At end of game, this card is worth 1 fewer VP for each card with cost 0 in your deck. (Minimum 0)"
    :copies 1}
   {:name "Power Girl"
    :cost 5
    :victory 2
    :power 3
    :copies 3}
   {:name "Stargirl"
    :cost 4
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card and put a card with cost 1 or greater from the destroyed pile on the bottom of the main deck."
    :power 2
    :copies 2}
   {:name "Steel"
    :cost 3
    :victory 1
    :text "You may destroy a non-Hero card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "Steve Trevor"
    :cost 1
    :victory 0
    :text "When you destroy this card in any zone, draw two cards, and then discard a card."
    :copies 2}
   {:name "Vibe"
    :cost 2
    :victory 1
    :power 1
    :text "You may put a Hero or Super Power with cost 5 or less from your discard pile on top of your deck."
    :copies 2}])

(def crisis1
  [{:name "Animal Man"
    :cost 4
    :victory 1
    :power 2
    :text "Choose a card type. Reveal the top card of your deck. If it has that card type, draw it."
    :copies 3}
   {:name "Captain Atom"
    :cost 6
    :victory 2
    :power "2?"
    :text "You may gain a card with cost 4 or less from the Line-Up, and then put it into any player's hand. If you choose not to, +2 Power."
    :copies 2}
   {:name "John Constantine"
    :cost 5
    :victory 1
    :text "Reveal the top two cards of your deck and draw one of them. Put the other on top of your deck or destroy it."
    :copies 2}])

(def all
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :hero))))
