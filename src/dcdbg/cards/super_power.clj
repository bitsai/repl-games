(ns dcdbg.cards.super-power)

(def base
  [{:name "Bulletproof"
    :cost 4
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card and you may destroy a card in your discard pile."
    :power 2
    :copies 3}
   {:name "Heat Vision"
    :cost 6
    :victory 2
    :power 3
    :text "You may destroy a card in your hand or discard pile."
    :copies 3}
   {:name "Super Speed"
    :cost 3
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :text "Draw a card."
    :copies 4}
   {:name "Super Strength"
    :cost 7
    :victory 2
    :power 5
    :copies 3}
   {:name "X-Ray Vision"
    :cost 3
    :victory 1
    :text "Each foe reveals the top card of his deck. You may play one of the non-Location cards revealed this way this turn, then return it to the top of its owner's deck."
    :copies 3}])

(def forever-evil
  [{:name "Bizarro Power"
    :cost 6
    :victory -1
    :attack "Each foe gains a Weakness."
    :power 4
    :text "Gain a Weakness."
    :copies 2}
   {:name "Constructs of Fear"
    :cost 7
    :victory 2
    :attack "Each foe discards two cards."
    :power 3
    :copies 2}
   {:name "Expert Marksman"
    :cost 3
    :victory 1
    :text "You may destroy a non-Super Power card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "Giant Growth"
    :cost 2
    :victory 1
    :power 2
    :copies 3}
   {:name "Insanity"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, you may put a card from your hand or discard pile into the attacker's hand."
    :text "If this is the first card you play this turn, each player passes a card in his hand to the hand of the player on his left."
    :copies 3}
   {:name "Invulnerable"
    :cost 3
    :victory 1
    :defense "When you are attacked, you may reveal this card from your hand. If you do, you may discard it or destroy a Vulnerability in your hand or discard pile to avoid an Attack."
    :power 1
    :copies 3}
   {:name "Power Drain"
    :cost 4
    :victory 1
    :attack "A foe reveals his hand. Choose one card revealed this way to be discarded."
    :power 2
    :copies 2}
   {:name "Super Intellect"
    :cost 4
    :victory 1
    :power "2+"
    :text "You may destroy a card in your hand or discard pile. If it's an Equipment, +Power equal to its cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Transmutation"
    :cost 5
    :victory 1
    :text "Destroy a card in your hand or discard pile and gain 1 VP. You may gain a card from the Line-Up of equal or lesser cost than the card destroyed this way."
    :copies 3}
   {:name "Ultra Strength"
    :cost 9
    :victory 3
    :power 3
    :text "Draw two cards."
    :copies 1}
   {:name "Word of Power"
    :cost 1
    :victory 0
    :text "When you destroy this card in any zone, you pay 4 less to defeat Super Heroes this turn."
    :copies 2}])

(def crisis1
  [{:name "Flight"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, you may put a card from your discard pile on top of your deck."
    :power 1
    :copies 4}
   {:name "Magic"
    :cost 5
    :victory 1
    :text "Choose a card you played this turn. Play it again this turn, and then destroy it. (Its effects remain.)"
    :copies 2}
   {:name "Power of the Red"
    :cost 4
    :victory 1
    :text "Discard any number of cards, then draw that many cards."
    :copies 3}])

(def all
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :super-power))))
