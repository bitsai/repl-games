(ns dcdbg.cards.super-villain)

(def crisis1
  [{:name "Ra's Al Ghul (IMP)"
    :cost 9
    :victory 4
    :text "At the end of your turn, put this card onto the bottom of its owner's deck before drawing a new hand."
    :power 4
    :stack-ongoing "At the end of your turn, if you did not buy or gain a card from the Line-Up, add the top card of the main deck to the Line-Up."
    :first-appearance-attack nil}
   {:name "Crisis Anti-Monitor (IMP)"
    :cost "13+"
    :victory nil
    :text nil
    :power nil
    :stack-ongoing "Add the top card of the main deck to the Line-Up. Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."
    :first-appearance-attack nil}
   {:name "Atrocitus (IMP)"
    :cost 11
    :victory 5
    :text "Destroy up to three cards in your discard pile."
    :power 3
    :stack-ongoing nil
    :first-appearance-attack "Each player puts his hand under his Super Hero. If you avoid this Attack, put a random card from your hand under your Super Hero. When this Villain is defeated, discard those cards."}
   {:name "Black Manta (IMP)"
    :cost "10+"
    :victory 5
    :text "You may gain all Equipment in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Black Manta costs 3 more to defeat for each Equipment in the Line-Up."
    :first-appearance-attack "Each player puts all Equipment from his hand into the Line-Up."}
   {:name "Brainiac (IMP)"
    :cost 12
    :victory 6
    :text "Each foe reveals two random cards from his hand. Play one revealed non-Location card from each foe's hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player discards a random card, and then destroys the top X cards of the main deck, where X is the cost of that card."}
   {:name "Captain Cold (IMP)"
    :cost 10
    :victory 5
    :text "An additional +1 Power for each foe with a Hero in his discard pile."
    :power "4+"
    :stack-ongoing nil
    :first-appearance-attack "Each player flips his Super Hero face down. When this Villain is defeated, each player may discard a random card. If you do, flip your Super Hero face up."}
   {:name "Darkseid (IMP)"
    :cost "10+"
    :victory 5
    :text "You may gain all Super Powers in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Darkseid costs 3 more to defeat for each Super Power in the Line-Up."
    :first-appearance-attack "Each player puts all Super Powers from his hand into the Line-Up."}
   {:name "Deathstroke (IMP)"
    :cost 10
    :victory 5
    :text "You may gain a Hero and Villain from the Line-Up. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing nil
    :first-appearance-attack "Each player destroys a Hero, Super Power, and Equipment in his hand."}
   {:name "Hades (IMP)"
    :cost 12
    :victory 6
    :text "Gain a card from the destroyed pile and put it into your hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player puts the top two cards of the main deck into the Line-Up. Gain Weaknesses equal to the cost of one of those cards."}
   {:name "The Joker (IMP)"
    :cost 10
    :victory 5
    :text "Each foe chooses: They discard two random cards or you draw two cards."
    :power 2
    :stack-ongoing nil
    :first-appearance-attack "Discard a random card. If its cost is 0, gain a Weakness and repeat this."}
   {:name "Lex Luthor (IMP)"
    :cost 11
    :victory 5
    :text "Draw four cards."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "This Attack can't be avoided. Each player gains X Weaknesses, where X is equal to the highest VP value among cards in his hand. (* = 3.)"}
   {:name "Parallax (IMP)"
    :cost 13
    :victory 6
    :text "Draw a card. Double your Power this turn."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player reveals his hand and discards all cards with cost 3 or less."}
   {:name "Sinestro (IMP)"
    :cost "10+"
    :victory 5
    :text "You may gain all Heroes in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Sinestro costs 3 more to defeat for each Hero in the Line-Up."
    :first-appearance-attack "Each player puts all Heroes from his hand into the Line-Up."}])

(def all
  (->> crisis1
       (map #(assoc % :type :super-villain))))
