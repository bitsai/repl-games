(ns dcdbg.cards.hero)

(def base
  [{:name "Blue Beetle"
    :cost 6
    :victory 2
    :text nil
    :power 3
    :defense "You may reveal this card from your hand to avoid an Attack. (It stays in your hand.)"
    :copies 1}
   {:name "Catwoman"
    :cost 2
    :victory 1
    :text nil
    :power 2
    :defense nil
    :copies 3}
   {:name "Dark Knight"
    :cost 5
    :victory 1
    :text "Gain all Equipment in the Line-Up. Then, if you play or have played Catwoman this turn, you may put a card you bought or gained this turn into your hand."
    :power 2
    :defense nil
    :copies 1}
   {:name "Emerald Knight"
    :cost 5
    :victory 1
    :text "Remove an Equipment, Hero, or Super Power from the Line-Up. Play it, then return it to the Line-Up at the end of your turn."
    :power nil
    :defense nil
    :copies 1}
   {:name "The Fastest Man Alive"
    :cost 5
    :victory 1
    :text "Draw two cards."
    :power nil
    :defense nil
    :copies 1}
   {:name "Green Arrow"
    :cost 5
    :victory "5?"
    :text "At the end of the game, if you have four or more other Heroes in your deck, this card is worth 5 VPs."
    :power 2
    :defense nil
    :copies 3}
   {:name "High-Tech Hero"
    :cost 3
    :victory 1
    :text "If you play or have played a Super Power or Equipment this turn, +3 Power. Otherwise, +1 Power."
    :power "1+"
    :defense nil
    :copies 3}
   {:name "J'onn J'onnz"
    :cost 6
    :victory 2
    :text "Play the top card of the Super-Villain stack, then return it to the top of the stack. (The First Appearance -- Attack does not happen.)"
    :power nil
    :defense nil
    :copies 1}
   {:name "Kid Flash"
    :cost 2
    :victory 1
    :text "Draw a card."
    :power nil
    :defense nil
    :copies 4}
   {:name "King of Atlantis"
    :cost 5
    :victory 1
    :text "You may destroy a card in your discard pile. If you do, +3 Power. Otherwise, +1 Power."
    :power "1+"
    :defense nil
    :copies 1}
   {:name "Man of Steel"
    :cost 8
    :victory 3
    :text "Put all Super Powers from your discard pile into your hand."
    :power 3
    :defense nil
    :copies 1}
   {:name "Mera"
    :cost 3
    :victory 1
    :text "If your discard pile is empty, +4 Power. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 2}
   {:name "Princess Diana of Themyscira"
    :cost 7
    :victory 2
    :text "Gain all Villains with cost 7 or less in the Line-Up."
    :power nil
    :defense nil
    :copies 1}
   {:name "Robin"
    :cost 3
    :victory 1
    :text "Put an Equipment from your discard pile into your hand."
    :power 1
    :defense nil
    :copies 3}
   {:name "Supergirl"
    :cost 4
    :victory 1
    :text "You may put a Kick from the Kick stack into your hand."
    :power nil
    :defense nil
    :copies 2}
   {:name "Swamp Thing"
    :cost 4
    :victory 1
    :text "If you control a Location, +5 Power. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 2}
   {:name "Zatanna Zatara"
    :cost 4
    :victory 1
    :text "You may put up to two cards from your discard pile on the bottom of your deck."
    :power 1
    :defense nil
    :copies 2}])

(def crisis1
  [{:name "Animal Man"
    :cost 4
    :victory 1
    :text "Choose a card type. Reveal the top card of your deck. If it has that card type, draw it."
    :power 2
    :defense nil
    :copies 3}
   {:name "Captain Atom"
    :cost 6
    :victory 2
    :text "You may gain a card with cost 4 or less from the Line-Up, and then put it into any player's hand. If you choose not to, +2 Power."
    :power "2?"
    :defense nil
    :copies 2}
   {:name "John Constantine"
    :cost 5
    :victory 1
    :text "Reveal the top two cards of your deck and draw one of them. Put the other on top of your deck or destroy it."
    :power nil
    :defense nil
    :copies 2}])

(def crossover1
  [{:name "Citizen Steel"
    :cost 5
    :victory 1
    :text "Draw a card. Super-Villains cost you 1 less to defeat this turn for each Punch you play or have played this turn."
    :power nil
    :defense nil
    :copies 1}
   {:name "Dr. Mid-Nite"
    :cost 4
    :victory 1
    :text "Look at the top two cards of your deck. You may discard any of them and put the rest back in any order."
    :power 2
    :defense nil
    :copies 1}
   {:name "Liberty Belle"
    :cost 3
    :victory 1
    :text nil
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw three cards and put two cards from your hand on top of your deck."
    :copies 1}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :hero))))
