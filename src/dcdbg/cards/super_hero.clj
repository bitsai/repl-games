(ns dcdbg.cards.super-hero)

(def forever-evil
  [{:name "The Flash"
    :cost 8
    :victory 4
    :text "Draw three cards, and then discard a card. (This card starts the game on top of the Super Hero stack)."}
   {:name "Aquaman"
    :cost 11
    :victory 6
    :attack "Each player puts four cards with cost 0 from his discard pile on top of his deck. If you put none there, gain a Weakness."
    :text "You may put up to three cards from your discard pile on top of your deck. If you choose not to, +3 Power."}
   {:name "Batman"
    :cost 11
    :victory 6
    :attack "Each player destroys an Equipment in his hand or discard pile. If you cannot, gain a Weakness."
    :text "You may play up to three Equipment with cost 6 or less from the destroyed pile, and then put them on the bottom of the main deck. If you chose not to, +3 Power."}
   {:name "Constantine"
    :cost 10
    :victory 5
    :attack "Each player loses 3 VPs. If you have none to lose, gain a Weakness."
    :text "Reveal the top three cards of your deck. Draw one, destroy one, and put one on top of your deck. Gain VPs equal to the destroyed card's VP value."}
   {:name "Cyborg"
    :cost 10
    :victory 5
    :attack "Each player discards a Super Power and an Equipment. If you discard no cards, gain a Weakness."
    :text "+2 Power for each Super Power and Equipment you play or have played."}
   {:name "Green Arrow"
    :cost 9
    :victory 5
    :attack "Each player discards two Punch cards. For each Punch you fail to discard, gain a Weakness."
    :text "When you play this card, leave it in front of you for the rest of the game. Ongoing: Punch cards you play have an additional +1 Power."}
   {:name "Green Lantern"
    :cost 11
    :victory 6
    :attack "Each player destroys a Hero in his hand or discard pile. If you cannot, gain a Weakness."
    :text "You may play up to three Heroes with cost 6 or less from the destroyed pile, and then put them on the bottom of the main deck. If you choose not to, +3 Power."}
   {:name "Martian Manhunter"
    :cost 12
    :victory 6
    :attack "Each player discards a Hero and a Villain. If you discard no cards, gain a Weakness."
    :text "+2 Power for each Hero and Villain you play or have played this turn."}
   {:name "Shazam!"
    :cost 12
    :victory 6
    :attack "Each player puts a card with cost 5 or greater from his hand or discard pile on the bottom of the main deck. If you cannot, gain a Weakness."
    :text "Gain the top two cards of the main deck, play them, and then destroy one of them. (Its effects remain.)"}
   {:name "Superman"
    :cost 13
    :victory 6
    :attack "Each player destroys a Super Power in his hand or discard pile. If you cannot, gain two Weakness cards."
    :text "You may play up to three Super Powers from the destroyed pile, and then put them on the bottom of the main deck. If you choose not to, +4 Power."}
   {:name "Swamp Thing"
    :cost 9
    :victory 5
    :attack "Each player puts a Location he controls into his discard pile. If you cannot, gain a Weakness."
    :text "+2 Power for each Location in play."}
   {:name "Wonder Woman"
    :cost 11
    :victory 6
    :attack "Each player destroys a Villain in his hand or discard pile. If you cannot, gain a Weakness."
    :text "You may play up to three Villains with cost 6 or less from the destroyed pile, and then put them on the bottom of the main deck. If you choose not to, +3 Power."}])

(def all
  (->> forever-evil
       (map #(assoc % :type :super-hero))))
