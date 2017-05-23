(ns dcdbg.cards.super-villain)

(def crisis1
  [{:name "Ra's Al Ghul (IM)"
    :cost 9
    :victory 4
    :ongoing "At the end of your turn, if you did not buy or gain a card from the Line-Up, add the top card of the main deck to the Line-Up."
    :power 4
    :text "At the end of your turn, put this card onto the bottom of its owner's deck before drawing a new hand."}
   {:name "Crisis Anti-Monitor (IM)"
    :cost "13+"
    :ongoing "Add the top card of the main deck to the Line-Up. Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."}
   {:name "Atrocitus (IM)"
    :cost 11
    :victory 5
    :attack "Each player puts his hand under his Super Hero. If you avoid this Attack, put a random card from your hand under your Super Hero. When this Villain is defeated, discard those cards."
    :power 3
    :text "Destroy up to three cards in your discard pile."}
   {:name "Black Manta (IM)"
    :cost "10+"
    :victory 5
    :attack "Each player puts all Equipment from his hand into the Line-Up."
    :ongoing "Black Manta costs 3 more to defeat for each Equipment in the Line-Up."
    :power "4?"
    :text "You may gain all Equipment in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."}
   {:name "Brainiac (IM)"
    :cost 12
    :victory 6
    :attack "Each player discards a random card, and then destroys the top X cards of the main deck, where X is the cost of that card."
    :text "Each foe reveals two random cards from his hand. Play one revealed non-Location card from each foe's hand."}
   {:name "Captain Cold (IM)"
    :cost 10
    :victory 5
    :attack "Each player flips his Super Hero face down. When this Villain is defeated, each player may discard a random card. If you do, flip your Super Hero face up."
    :power "4+"
    :text "An additional +1 Power for each foe with a Hero in his discard pile."}
   {:name "Darkseid (IM)"
    :cost "10+"
    :victory 5
    :attack "Each player puts all Super Powers from his hand into the Line-Up."
    :ongoing "Darkseid costs 3 more to defeat for each Super Power in the Line-Up."
    :power "4?"
    :text "You may gain all Super Powers in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."}
   {:name "Deathstroke (IM)"
    :cost 10
    :victory 5
    :attack "Each player destroys a Hero, Super Power, and Equipment in his hand."
    :power "4?"
    :text "You may gain a Hero and Villain from the Line-Up. If you choose not to, +4 Power."}
   {:name "Hades (IM)"
    :cost 12
    :victory 6
    :attack "Each player puts the top two cards of the main deck into the Line-Up. Gain Weaknesses equal to the cost of one of those cards."
    :text "Gain a card from the destroyed pile and put it into your hand."}
   {:name "The Joker (IM)"
    :cost 10
    :victory 5
    :attack "Discard a random card. If its cost is 0, gain a Weakness and repeat this."
    :power 2
    :text "Each foe chooses: They discard two random cards or you draw two cards."}
   {:name "Lex Luthor (IM)"
    :cost 11
    :victory 5
    :attack "This Attack can't be avoided. Each player gains X Weaknesses, where X is equal to the highest VP value among cards in his hand. (* = 3.)"
    :text "Draw four cards."}
   {:name "Parallax (IM)"
    :cost 13
    :victory 6
    :attack "Each player reveals his hand and discards all cards with cost 3 or less."
    :text "Draw a card. Double your Power this turn."}
   {:name "Sinestro (IM)"
    :cost "10+"
    :victory 5
    :attack "Each player puts all Heroes from his hand into the Line-Up."
    :ongoing "Sinestro costs 3 more to defeat for each Hero in the Line-Up."
    :power "4?"
    :text "You may gain all Heroes in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."}])

(def all
  (->> crisis1
       (map #(assoc % :type :villain))))
