(ns dcdbg.cards.villain)

(def base
  [{:name "Bane"
    :cost 4
    :victory 1
    :text nil
    :power 2
    :attack "Each foe chooses and discards a card."
    :copies 2}
   {:name "Bizarro"
    :cost 7
    :victory "0+"
    :text "At the end of the game, this card is worth 2 VPs for each Weakness in your deck."
    :power 3
    :attack nil
    :copies 1}
   {:name "Cheetah"
    :cost 2
    :victory 1
    :text "Gain any card with cost 4 or less from the Line-Up."
    :power nil
    :attack nil
    :copies 2}
   {:name "Clayface"
    :cost 4
    :victory 1
    :text "Choose a card you played this turn. Play it again this turn. (Effects and Power generated the first time you played it remain.)"
    :power nil
    :attack nil
    :copies 2}
   {:name "Doomsday"
    :cost 6
    :victory 2
    :text nil
    :power 4
    :attack nil
    :copies 2}
   {:name "Gorilla Grodd"
    :cost 5
    :victory 2
    :text nil
    :power 3
    :attack nil
    :copies 2}
   {:name "Harley Quinn"
    :cost 2
    :victory 1
    :text nil
    :power 1
    :attack "Each foe puts a Punch or Vulnerability from his discard pile on top of his deck."
    :copies 2}
   {:name "Lobo"
    :cost 7
    :victory 1
    :text "You may destroy up to two cards in your hand and/or discard pile."
    :power 3
    :attack nil
    :copies 1}
   {:name "The Penguin"
    :cost 3
    :victory 1
    :text "Draw two cards, then choose and discard two cards from your hand."
    :power nil
    :attack nil
    :copies 2}
   {:name "Poison Ivy"
    :cost 3
    :victory 1
    :text nil
    :power 1
    :attack "Each foe discards the top card of his deck. If its cost is 1 or greater, that player gains a Weakness."
    :copies 2}
   {:name "The Riddler"
    :cost 3
    :victory 1
    :text "You may pay 3 Power. If you do, gain the top card of the main deck. Use this ability any number of times this turn. If you choose not to, +1 Power instead."
    :power "1?"
    :attack nil
    :copies 2}
   {:name "Scarecrow"
    :cost 5
    :victory 1
    :text nil
    :power 2
    :attack "Each foe gains a Weakness."
    :copies 2}
   {:name "Solomon Grundy"
    :cost 6
    :victory 2
    :text "When you buy or gain this Villain, you may put him on top of your deck."
    :power 3
    :attack nil
    :copies 2}
   {:name "Starro"
    :cost 7
    :victory 2
    :text nil
    :power nil
    :attack "Each foe discards the top card of his deck. You may play each non-Location discarded this way this turn."
    :copies 1}
   {:name "Suicide Squad"
    :cost 4
    :victory "0+"
    :text "If you already played two other Suicide Squad cards this turn, each foe discards his hand. At the end of the game, this card is worth 1 VP for each Suicide Squad in your deck."
    :power 2
    :attack nil
    :copies 6}
   {:name "Two-Face"
    :cost 2
    :victory 1
    :text "Choose even or odd, then reveal the top card of your deck. If its cost matches your choice, draw it. If not, discard it. (0 is even.)"
    :power 1
    :attack nil
    :copies 2}])

(def crisis1
  [{:name "Avatar of the Rot"
    :cost 7
    :victory 3
    :text nil
    :power 3
    :attack "Each foe gains two Weakness cards."
    :copies 1}
   {:name "Killer Frost"
    :cost 4
    :victory 1
    :text nil
    :power 1
    :attack "Each foe puts a Location he controls into his discard pile. If none do, draw a card."
    :copies 2}
   {:name "Psycho Pirate"
    :cost 4
    :victory 1
    :text nil
    :power 2
    :attack "Each foe reveals his hand and discards cards until his hand contains no duplicate cards."
    :copies 2}
   {:name "Strife"
    :cost 3
    :victory 1
    :text "You may gain a card from the Line-Up with the same name as a card in your hand. If you choose not to, +1 Power."
    :power "1?"
    :attack "Each foe reveals his hand and discards each card with the same name as a card in the Line-Up."
    :copies 2}])

(def crossover1
  [{:name "Per Degaton"
    :cost 5
    :victory 1
    :text "Discard any number of cards from your hand. +1 Power for each card you discard or have discarded this turn."
    :power 2
    :attack nil
    :copies 1}
   {:name "Scythe"
    :cost 3
    :victory 1
    :text nil
    :power 2
    :attack "Each foe gains a Weakness unless he reveals a Starter from his hand."
    :copies 1}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :villain))))
