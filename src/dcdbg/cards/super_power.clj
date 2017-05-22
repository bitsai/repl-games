(ns dcdbg.cards.super-power)

(def base
  [{:name "Bulletproof"
    :cost 4
    :victory 1
    :text nil
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw a card and you may destroy a card in your discard pile."
    :copies 3}
   {:name "Heat Vision"
    :cost 6
    :victory 2
    :text "You may destroy a card in your hand or discard pile."
    :power 3
    :defense nil
    :copies 3}
   {:name "Super Speed"
    :cost 3
    :victory 1
    :text "Draw a card."
    :power nil
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :copies 4}
   {:name "Super Strength"
    :cost 7
    :victory 2
    :text nil
    :power 5
    :defense nil
    :copies 3}
   {:name "X-Ray Vision"
    :cost 3
    :victory 1
    :text "Each foe reveals the top card of his deck. You may play one of the non-Location cards revealed this way this turn, then return it to the top of its owner's deck."
    :power nil
    :defense nil
    :copies 3}])

(def crisis1
  [{:name "Flight"
    :cost 2
    :victory 1
    :text nil
    :power 1
    :defense "You may discard this card to avoid an Attack. If you do, you may put a card from your discard pile on top of your deck."
    :copies 4}
   {:name "Magic"
    :cost 5
    :victory 1
    :text "Choose a card you played this turn. Play it again this turn, and then destroy it. (Its effects remain.)"
    :power nil
    :defense nil
    :copies 2}
   {:name "Power of the Red"
    :cost 4
    :victory 1
    :text "Discard any number of cards, then draw that many cards."
    :power nil
    :defense nil
    :copies 3}])

(def all
  (->> (concat base crisis1)
       (map #(assoc % :type :super-power))))
