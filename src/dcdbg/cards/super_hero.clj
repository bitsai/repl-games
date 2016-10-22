(ns dcdbg.cards.super-hero)

(def base
  [{:name "The Flash"
    :text "You go first. The first time a card tells you to draw one or more cards during each of your turns, draw an additional card."}
   {:name "Superman"
    :text "+1 Power for each different Super Power you play during your turn."}
   {:name "Batman"
    :text "+1 Power for each Equipment you play during your turn."}
   {:name "Wonder Woman"
    :text "For each Villain you buy or gain during your turn, draw an extra card at the end of your turn."}
   {:name "Green Lantern"
    :text "If you play three or more cards with different names and cost 1 or more during your turn, +3 Power."}
   {:name "Aquaman"
    :text "You may put any cards with cost 5 or less you buy or gain during your turn on top of your deck."}
   {:name "Cyborg"
    :text "If you play one or more Super Powers during your turn, +1 Power. If you play one or more Equipment during your turn, draw a card."}
   {:name "Martian Manhunter"
    :text "If you play two or more Villains during your turn, +3 Power. If you play two or more Heroes during your turn, +3 Power."}])

(def crisis1
  [{:name "The Flash (Crisis)"
    :text "You go first. Once during each of your turns, if you have played eight or more cards, choose a player. That player draws a card."}
   {:name "Superman (Crisis)"
    :text "Once during each of your turns, if you play 7 or greater cost worth of Super Powers, choose a player. That player draws a card."}
   {:name "Batman (Crisis)"
    :text "Once during each of your turns, if you have played two or more Equipment, you may choose a player. That player discards his hand, and then draws one fewer cards than he discarded."}
   {:name "Wonder Woman (Crisis)"
    :text "When you buy or gain a Villain from the Line-Up, choose a player. That player may destroy a Vulnerability or Weakness in his hand or discard pile."}
   {:name "Green Lantern (Crisis)"
    :text "When any player is Attacked, you may discard three cards. If you do, that player avoids the Attack."}
   {:name "Aquaman (Crisis)"
    :text "When you buy or gain a card with cost 5 or less during your turn, you may place it into any player's discard pile."}
   {:name "Cyborg (Crisis)"
    :text "Once during each of your turns, if you have played a Super Power and Equipment, you may put a card with cost 1 or greater from any player's discard pile on the bottom of the main deck."}
   {:name "Martian Manhunter (Crisis)"
    :text "Once during each of your turns, if you have played two or more Heroes, choose a player. That player may put a Hero from his discard pile on top of his deck."}
   {:name "Animal Man"
    :text "During each of your turns, if you have played three or more different card types, +2 Power."}
   {:name "Constantine"
    :text "Once during each of your turns, reveal the top card of your deck. If its cost is 1 or greater, you may play it and it has +1 Power. If you do, destroy it at the end of your turn."}
   {:name "Green Arrow"
    :text "Once during each of your turns, you may discard a Punch card. If you do, you pay 2 less to defeat Villains and Super-Villains this turn."}
   {:name "Robin"
    :text "The first time a card tells you to draw a card during each of your turns, you may instead put an Equipment from your discard pile into your hand."}
   {:name "Swamp Thing"
    :text "During your turn, you are considered to control each Location in the Line-Up and in play."}
   {:name "Zatanna Zatara"
    :text "Once during each of your turns, you may put a card with cost 1 or greater from your hand on the bottom of your deck. If you do, draw a card."}])

(def crossover1
  [{:name "Mister Terrific"
    :text "Once during each of your turns, you may discard a Punch. If you do, reveal the top three cards of your deck, draw one Equipment revealed this way, and put the rest back in any order."}
   {:name "Wildcat"
    :text "The first time you play a Punch during each of your turns: If you have played a Hero this turn, draw a card. If you have played a Villain this turn, draw a card."}
   {:name "Jay Garrick"
    :text "When a card tells you to draw one or more cards, before drawing, reveal the top card of your deck and you may discard it."}
   {:name "Alan Scott"
    :text "Each time you play a different Super Power during your turn, reveal the top card of your deck. If the revealed card costs 0, draw it."}
   {:name "Doctor Fate"
    :text "When you play two cards with consecutive costs during your turn, +1 Power. When you play three cards with consecutive costs during your turn, draw a card."}
   {:name "Power Girl"
    :text "Each time you play a different Super Power during your turn, put a Punch from your discard pile into your hand."}
   {:name "Stargirl"
    :text "When you play a Defense card during your turn or avoid an Attack, you may draw a card and then discard a card."}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :super-hero))))
