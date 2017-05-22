(ns dcdbg.cards.villain)

(def base
  [{:name "Bane"
    :cost 4
    :victory 1
    :attack "Each foe chooses and discards a card."
    :power 2
    :copies 2}
   {:name "Bizarro"
    :cost 7
    :victory "0+"
    :power 3
    :text "At the end of the game, this card is worth 2 VPs for each Weakness in your deck."
    :copies 1}
   {:name "Cheetah"
    :cost 2
    :victory 1
    :text "Gain any card with cost 4 or less from the Line-Up."
    :copies 2}
   {:name "Clayface"
    :cost 4
    :victory 1
    :text "Choose a card you played this turn. Play it again this turn. (Effects and Power generated the first time you played it remain.)"
    :copies 2}
   {:name "Doomsday"
    :cost 6
    :victory 2
    :power 4
    :copies 2}
   {:name "Gorilla Grodd"
    :cost 5
    :victory 2
    :power 3
    :copies 2}
   {:name "Harley Quinn"
    :cost 2
    :victory 1
    :attack "Each foe puts a Punch or Vulnerability from his discard pile on top of his deck."
    :power 1
    :copies 2}
   {:name "Lobo"
    :cost 7
    :victory 1
    :power 3
    :text "You may destroy up to two cards in your hand and/or discard pile."
    :copies 1}
   {:name "The Penguin"
    :cost 3
    :victory 1
    :text "Draw two cards, then choose and discard two cards from your hand."
    :copies 2}
   {:name "Poison Ivy"
    :cost 3
    :victory 1
    :attack "Each foe discards the top card of his deck. If its cost is 1 or greater, that player gains a Weakness."
    :power 1
    :copies 2}
   {:name "The Riddler"
    :cost 3
    :victory 1
    :power "1?"
    :text "You may pay 3 Power. If you do, gain the top card of the main deck. Use this ability any number of times this turn. If you choose not to, +1 Power instead."
    :copies 2}
   {:name "Scarecrow"
    :cost 5
    :victory 1
    :attack "Each foe gains a Weakness."
    :power 2
    :copies 2}
   {:name "Solomon Grundy"
    :cost 6
    :victory 2
    :power 3
    :text "When you buy or gain this Villain, you may put him on top of your deck."
    :copies 2}
   {:name "Starro"
    :cost 7
    :victory 2
    :attack "Each foe discards the top card of his deck. You may play each non-Location discarded this way this turn."
    :copies 1}
   {:name "Suicide Squad"
    :cost 4
    :victory "0+"
    :power 2
    :text "If you already played two other Suicide Squad cards this turn, each foe discards his hand. At the end of the game, this card is worth 1 VP for each Suicide Squad in your deck."
    :copies 6}
   {:name "Two-Face"
    :cost 2
    :victory 1
    :power 1
    :text "Choose even or odd, then reveal the top card of your deck. If its cost matches your choice, draw it. If not, discard it. (0 is even.)"
    :copies 2}])

(def forever-evil
  [{:name "Atomica"
    :cost 3
    :victory 1
    :text "You may destroy a non-Villain card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "The Blight"
    :cost 4
    :victory 1
    :power "2+"
    :text "You may destroy a card in your hand or discard pile. If it's a Super Power, +Power equal to its cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Deathstorm"
    :cost 4
    :victory "10-"
    :text "You may destroy a card in your hand. At end of game, this card is worth 1 fewer VP for each card in excess of 20 in your deck. (Minimum 0.)"
    :copies 1}
   {:name "Despero"
    :cost 6
    :victory 2
    :attack "A foe discards a card with cost 1 or greater."
    :text "Draw two cards."
    :copies 1}
   {:name "Emperor Penguin"
    :cost 1
    :victory 0
    :text "When you destroy this card in any zone, gain 2 VPs."
    :copies 2}
   {:name "Giganta"
    :cost 4
    :victory 1
    :power "2+"
    :text "+4 Power if there there are no cards with cost 3 or less in your discard pile. Otherwise, +2 Power."
    :copies 2}
   {:name "Grid"
    :cost 2
    :victory 1
    :power 1
    :text "You may put a Villain or Equipment with cost 5 or less from your discard pile on top of your deck."
    :copies 2}
   {:name "Johnny Quick"
    :cost 2
    :victory 1
    :text "Draw a card."
    :copies 2}
   {:name "Man-Bat"
    :cost 3
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, steal 1 VP from the attacker."
    :power 2
    :copies 2}
   {:name "Owlman"
    :cost 6
    :victory 2
    :power "0+"
    :text "You may destroy an Equipment in the Line-Up. +1 Power for each different Equipment in the destroyed pile."
    :copies 1}
   {:name "Power Ring"
    :cost 6
    :victory 2
    :power "0+"
    :text "You may destroy a Hero in the Line-Up. +1 Power for each different Hero in the destroyed pile."
    :copies 1}
   {:name "Royal Flush Gang"
    :cost 5
    :victory 0
    :text "Draw two cards, and then discard two cards. Gain 1 VP for each other Royal Flush Gang you play or have played this turn."
    :copies 5}
   {:name "Superwoman"
    :cost 7
    :victory 3
    :power "0+"
    :text "You may destroy a Villain in the Line-Up. +1 Power for each different Villain in the destroyed pile."
    :copies 1}
   {:name "Ultraman"
    :cost 8
    :victory 3
    :power "0+"
    :text "You may destroy a card in the Line-Up. +1 Power for each different Super Power in the destroyed pile."
    :copies 1}])

(def crisis1
  [{:name "Avatar of the Rot"
    :cost 7
    :victory 3
    :attack "Each foe gains two Weakness cards."
    :power 3
    :copies 1}
   {:name "Killer Frost"
    :cost 4
    :victory 1
    :attack "Each foe puts a Location he controls into his discard pile. If none do, draw a card."
    :power 1
    :copies 2}
   {:name "Psycho Pirate"
    :cost 4
    :victory 1
    :attack "Each foe reveals his hand and discards cards until his hand contains no duplicate cards."
    :power 2
    :copies 2}
   {:name "Strife"
    :cost 3
    :victory 1
    :attack "Each foe reveals his hand and discards each card with the same name as a card in the Line-Up."
    :power "1?"
    :text "You may gain a card from the Line-Up with the same name as a card in your hand. If you choose not to, +1 Power."
    :copies 2}])

(def all
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :villain))))
