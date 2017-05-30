(ns dcdbg.cards.compiled)

(def cards
[{:set :base,
  :name "Blue Beetle",
  :type :hero,
  :cost 6,
  :victory 2,
  :copies 1,
  :defense
  "You may reveal this card from your hand  to avoid an Attack. (It stays in your hand.)",
  :power 3}
 {:set :base,
  :name "Catwoman",
  :type :hero,
  :cost 2,
  :victory 1,
  :copies 3,
  :power 2}
 {:set :base,
  :name "Dark Knight",
  :type :hero,
  :cost 5,
  :victory 1,
  :copies 1,
  :text
  "Gain all Equipment in the Line-Up. Then, if you play or have played Catwoman this turn, you may put a card you bought or gained this turn into your hand.",
  :power 2}
 {:set :base,
  :name "Emerald Knight",
  :type :hero,
  :cost 5,
  :victory 1,
  :copies 1,
  :text
  "Remove an Equipment, Hero, or Super Power from the Line-Up. Play it, then return it to the Line-Up at the end of your turn."}
 {:set :base,
  :name "The Fastest Man Alive",
  :type :hero,
  :cost 5,
  :victory 1,
  :copies 1,
  :text "Draw two cards."}
 {:set :base,
  :name "Green Arrow",
  :type :hero,
  :cost 5,
  :victory "*",
  :copies 3,
  :text
  "At the end of the game, if you have four or more other Heroes in your deck, this card is worth 5 VPs.",
  :power 2}
 {:set :base,
  :name "High-Tech Hero",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 3,
  :text
  "If you play or have played a Super Power or Equipment this turn, +3 Power. Otherwise, +1 Power.",
  :power "*"}
 {:set :base,
  :name "J'onn J'onnz",
  :type :hero,
  :cost 6,
  :victory 2,
  :copies 1,
  :text
  "Play the top card of the Super-Villain stack, then return it to the top of the stack. (The First Appearance -- Attack does not happen.)"}
 {:set :base,
  :name "Kid Flash",
  :type :hero,
  :cost 2,
  :victory 1,
  :copies 4,
  :text "Draw a card."}
 {:set :base,
  :name "King of Atlantis",
  :type :hero,
  :cost 5,
  :victory 1,
  :copies 1,
  :text
  "You may destroy a card in your discard pile. If you do, +3 Power. Otherwise, +1 Power.",
  :power "*"}
 {:set :base,
  :name "Man of Steel",
  :type :hero,
  :cost 8,
  :victory 3,
  :copies 1,
  :text "Put all Super Powers from your discard pile into your hand.",
  :power 3}
 {:set :base,
  :name "Mera",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "If your discard pile is empty, +4 Power. Otherwise, +2 Power.",
  :power "*"}
 {:set :base,
  :name "Princess Diana of Themyscira",
  :type :hero,
  :cost 7,
  :victory 2,
  :copies 1,
  :text "Gain all Villains with cost 7 or less in the Line-Up."}
 {:set :base,
  :name "Robin",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 3,
  :text "Put an Equipment from your discard pile into your hand.",
  :power 1}
 {:set :base,
  :name "Supergirl",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :text "You may put a Kick from the Kick stack into your hand."}
 {:set :base,
  :name "Swamp Thing",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :text "If you control a Location, +5 Power. Otherwise, +2 Power.",
  :power "*"}
 {:set :base,
  :name "Zatanna Zatara",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may put up to two cards from your discard pile on the bottom of your deck.",
  :power 1}
 {:set :base,
  :name "Bane",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :attack "Each foe chooses and discards a card.",
  :power 2}
 {:set :base,
  :name "Bizarro",
  :type :villain,
  :cost 7,
  :victory "*",
  :copies 1,
  :text
  "At the end of the game, this card is worth 2 VPs for each Weakness in your deck.",
  :power 3}
 {:set :base,
  :name "Cheetah",
  :type :villain,
  :cost 2,
  :victory 1,
  :copies 2,
  :text "Gain any card with cost 4 or less from the Line-Up."}
 {:set :base,
  :name "Clayface",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "Choose a card you played this turn. Play it again this turn. (Effects and Power generated the first time you played it remain.)"}
 {:set :base,
  :name "Doomsday",
  :type :villain,
  :cost 6,
  :victory 2,
  :copies 2,
  :power 4}
 {:set :base,
  :name "Gorilla Grodd",
  :type :villain,
  :cost 5,
  :victory 2,
  :copies 2,
  :power 3}
 {:set :base,
  :name "Harley Quinn",
  :type :villain,
  :cost 2,
  :victory 1,
  :copies 2,
  :attack
  "Each foe puts a Punch or Vulnerability from his discard pile on top of his deck.",
  :power 1}
 {:set :base,
  :name "Lobo",
  :type :villain,
  :cost 7,
  :victory 1,
  :copies 1,
  :text
  "You may destroy up to two cards in your hand and/or discard pile.",
  :power 3}
 {:set :base,
  :name "The Penguin",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "Draw two cards, then choose and discard two cards from your hand."}
 {:set :base,
  :name "Poison Ivy",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :attack
  "Each foe discards the top card of his deck. If its cost is 1 or greater, that player gains a Weakness.",
  :power 1}
 {:set :base,
  :name "The Riddler",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may pay 3 Power. If you do, gain the top card of the main deck. Use this ability any number of times this turn. If you choose not to, +1 Power instead.",
  :power "*"}
 {:set :base,
  :name "Scarecrow",
  :type :villain,
  :cost 5,
  :victory 1,
  :copies 2,
  :attack "Each foe gains a Weakness.",
  :power 2}
 {:set :base,
  :name "Solomon Grundy",
  :type :villain,
  :cost 6,
  :victory 2,
  :copies 2,
  :text
  "When you buy or gain this Villain, you may put him on top of your deck.",
  :power 3}
 {:set :base,
  :name "Starro",
  :type :villain,
  :cost 7,
  :victory 2,
  :copies 1,
  :attack
  "Each foe discards the top card of his deck. You may play each non-Location discarded this way this turn."}
 {:set :base,
  :name "Suicide Squad",
  :type :villain,
  :cost 4,
  :victory "*",
  :copies 6,
  :text
  "If you already played two other Suicide Squad cards this turn, each foe discards his hand. At the end of the game, this card is worth 1 VP for each Suicide Squad in your deck.",
  :power 2}
 {:set :base,
  :name "Two-Face",
  :type :villain,
  :cost 2,
  :victory 1,
  :copies 2,
  :text
  "Choose even or odd, then reveal the top card of your deck. If its cost matches your choice, draw it. If not, discard it. (0 is even.)",
  :power 1}
 {:set :base,
  :name "Bulletproof",
  :type :super-power,
  :cost 4,
  :victory 1,
  :copies 3,
  :defense
  "You may discard this card to avoid an Attack. If you do, draw a card and you may destroy a card in your discard pile.",
  :power 2}
 {:set :base,
  :name "Heat Vision",
  :type :super-power,
  :cost 6,
  :victory 2,
  :copies 3,
  :text "You may destroy a card in your hand or discard pile.",
  :power 3}
 {:set :base,
  :name "Super Speed",
  :type :super-power,
  :cost 3,
  :victory 1,
  :copies 4,
  :text "Draw a card.",
  :defense
  "You may discard this card to avoid an Attack. If you do, draw two cards."}
 {:set :base,
  :name "Super Strength",
  :type :super-power,
  :cost 7,
  :victory 2,
  :copies 3,
  :power 5}
 {:set :base,
  :name "X-Ray Vision",
  :type :super-power,
  :cost 3,
  :victory 1,
  :copies 3,
  :text
  "Each foe reveals the top card of his deck. You may play one of the non-Location cards revealed this way this turn, then return it to the top of its owner's deck."}
 {:set :base,
  :name "Aquaman's Trident",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 3,
  :text
  "You may put any one card you buy or gain this turn on top of your deck.",
  :power 2}
 {:set :base,
  :name "Batmobile",
  :type :equipment,
  :cost 2,
  :victory 1,
  :copies 3,
  :text
  "If this is the first card you play this turn, discard your hand and draw five cards. Otherwise, +1 Power.",
  :power "*"}
 {:set :base,
  :name "The Bat-Signal",
  :type :equipment,
  :cost 5,
  :victory 1,
  :copies 3,
  :text "Put a Hero from your discard pile into your hand.",
  :power 1}
 {:set :base,
  :name "The Cape and Cowl",
  :type :equipment,
  :cost 4,
  :victory 1,
  :copies 3,
  :defense
  "You may discard this card to avoid an Attack. If you do, draw two cards.",
  :power 2}
 {:set :base,
  :name "Green Arrow's Bow",
  :type :equipment,
  :cost 4,
  :victory 1,
  :copies 3,
  :text "Super-Villains cost you 2 less to defeat this turn.",
  :power 2}
 {:set :base,
  :name "Lasso of Truth",
  :type :equipment,
  :cost 2,
  :victory 1,
  :copies 3,
  :defense
  "You may discard this card to avoid an Attack. If you do, draw a card.",
  :power 1}
 {:set :base,
  :name "Nth Metal",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 4,
  :text "Look at the top card of your deck. You may destroy it.",
  :power 1}
 {:set :base,
  :name "Power Ring",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 3,
  :text
  "Reveal the top card of your deck. If its cost is 1 or greater, +3 Power. Otherwise, +2 Power.",
  :power "*"}
 {:set :base,
  :name "Utility Belt",
  :type :equipment,
  :cost 5,
  :victory "*",
  :copies 3,
  :text
  "At the end of the game, if you have four or more other Equipment in your deck, this card is worth 5 VPs.",
  :power 2}
 {:set :base,
  :name "Arkham Asylum",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing
  "When you play your first Villain on each of your turns, draw a card."}
 {:set :base,
  :name "The Batcave",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing
  "When you play your first Equipment on each of your turns, draw a card."}
 {:set :base,
  :name "Fortress of Solitude",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing
  "When you play your first Super Power on each of your turns, draw a card."}
 {:set :base,
  :name "Titans Tower",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing
  "When you play your first card with cost 2 or 3 on each of your turns, draw a card."}
 {:set :base,
  :name "The Watchtower",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing
  "When you play your first Hero on each of your turns, draw a card."}
 {:set :base,
  :name "Ra's al Ghul",
  :type :super-villain,
  :cost 8,
  :victory 4,
  :text
  "At the end of your turn, put this card on the bottom of its owner's deck before drawing a new hand. (This card starts the game on top of the Super-Villain stack.)",
  :power 3}
 {:set :base,
  :name "The Anti-Monitor",
  :type :super-villain,
  :cost 12,
  :victory 6,
  :text
  "Destroy any number of cards in the Line-Up, then replace them.",
  :attack
  "Each player reveals his hand, chooses a card with cost 1 or greater from it, and adds that card to the Line-Up.",
  :power 2}
 {:set :base,
  :name "Atrocitus",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text "Destroy up to two cards in your discard pile.",
  :attack
  "Each player puts a random card from his hand under his Super Hero. When this Villain is defeated, put each of those cards on top of its owner's deck.",
  :power 2}
 {:set :base,
  :name "Black Manta",
  :type :super-villain,
  :cost 8,
  :victory 4,
  :text "Draw a card.",
  :attack
  "Each player discards the top card of his deck. If you discarded a card with cost 1 or more, choose one: Destroy it; or discard your hand.",
  :power 3}
 {:set :base,
  :name "Brainiac",
  :type :super-villain,
  :cost 11,
  :victory 6,
  :text
  "Each foe reveals a random card from his hand. Play each revealed non-Location.",
  :attack
  "Each player chooses two cards from his hand and puts them on the table face down. Shuffle all of the chosen cards face down, then deal two back to each player at random."}
 {:set :base,
  :name "Captain Cold",
  :type :super-villain,
  :cost 9,
  :victory 5,
  :text
  "An additional +1 Power for each foe with a Hero in his discard pile.",
  :attack
  "Each player flips his Super Hero face down until this Villain is defeated.",
  :power 2}
 {:set :base,
  :name "Darkseid",
  :type :super-villain,
  :cost 11,
  :victory 6,
  :text
  "You may destroy two cards in your hand. If you do, +5 Power. Otherwise +3 Power.",
  :attack
  "Each player discards two cards unless he reveals a Villain from his hand.",
  :power "*"}
 {:set :base,
  :name "Deathstroke",
  :type :super-villain,
  :cost 9,
  :victory 5,
  :text
  "You may gain a Hero or Villain from the Line-Up. If you choose not to, +3 Power.",
  :attack
  "Each player reveals his hand and destroys a Hero, Super Power, or Equipment in his hand or discard pile.",
  :power "*"}
 {:set :base,
  :name "The Joker",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text
  "Each foe chooses, He discards a random card; or you draw a card.",
  :attack
  "Each player puts a card from his hand into the discard pile of the player on his left. If the card you received has a cost of 1 or greater, put a Weakness on top of your deck.",
  :power 2}
 {:set :base,
  :name "Lex Luthor",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text "Draw three cards.",
  :attack
  "Each player gains a Weakness for each Villain in the Line-Up."}
 {:set :base,
  :name "Parallax",
  :type :super-villain,
  :cost 12,
  :victory 6,
  :text "Double your Power this turn.",
  :attack
  "Each player reveals his hand and discards all cards with cost 2 or less."}
 {:set :base,
  :name "Sinestro",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text
  "Reveal the top card of the main deck. If it's a Hero, +3 Power and then destroy it. Otherwise, put it into your hand.",
  :attack
  "Each player reveals his hand and discards a card for each Hero he revealed this way.",
  :power "*"}
 {:set :base,
  :name "The Flash",
  :type :super-hero,
  :text
  "You go first. The first time a card tells you to draw one or more cards during each of your turns, draw an additional card."}
 {:set :base,
  :name "Superman",
  :type :super-hero,
  :text
  "+1 Power for each different Super Power you play during your turn.",
  :power "*"}
 {:set :base,
  :name "Batman",
  :type :super-hero,
  :text "+1 Power for each Equipment you play during your turn.",
  :power "*"}
 {:set :base,
  :name "Wonder Woman",
  :type :super-hero,
  :text
  "For each Villain you buy or gain during your turn, draw an extra card at the end of your turn."}
 {:set :base,
  :name "Green Lantern",
  :type :super-hero,
  :text
  "If you play three or more cards with different names and cost 1 or more during your turn, +3 Power.",
  :power "*"}
 {:set :base,
  :name "Aquaman",
  :type :super-hero,
  :text
  "You may put any cards with cost 5 or less you buy or gain during your turn on top of your deck."}
 {:set :base,
  :name "Cyborg",
  :type :super-hero,
  :text
  "If you play one or more Super Powers during your turn, +1 Power. If you play one or more Equipment during your turn, draw a card.",
  :power "*"}
 {:set :base,
  :name "Martian Manhunter",
  :type :super-hero,
  :text
  "If you play two or more Villains during your turn, +3 Power. If you play two or more Heroes during your turn, +3 Power.",
  :power "*"}
 {:set :crisis-1,
  :name "Animal Man",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 3,
  :text
  "Choose a card type. Reveal the top card of your deck. If it has that card type, draw it.",
  :power 2}
 {:set :crisis-1,
  :name "Captain Atom",
  :type :hero,
  :cost 6,
  :victory 2,
  :copies 2,
  :text
  "You may gain a card with cost 4 or less from the Line-Up, and then put it into any player's hand. If you choose not, to +2 Power.",
  :power "*"}
 {:set :crisis-1,
  :name "John Constantine",
  :type :hero,
  :cost 5,
  :victory 1,
  :copies 2,
  :text
  "Reveal the top two cards of your deck and draw one of them. Put the other on top of your deck or destroy it."}
 {:set :crisis-1,
  :name "Avatar of the Rot",
  :type :villain,
  :cost 7,
  :victory 3,
  :copies 1,
  :attack "Each foe gains two Weakness cards.",
  :power 3}
 {:set :crisis-1,
  :name "Killer Frost",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :attack
  "Each foe puts a Location he controls into his discard pile. If none do, draw a card.",
  :power 1}
 {:set :crisis-1,
  :name "Psycho Pirate",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "Each foe reveals his hand and discards cards until his hand contains no duplicate cards.",
  :power 2}
 {:set :crisis-1,
  :name "Strife",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may gain a card from the Line-Up with the same name as a card in your hand. If you choose not to, +1 Power.",
  :attack
  "Each foe reveals his hand and discards each card with the same name as a card in the Line-Up.",
  :power "*"}
 {:set :crisis-1,
  :name "Flight",
  :type :super-power,
  :cost 2,
  :victory 1,
  :copies 4,
  :defense
  "You may discard this card to avoid an Attack. If you do, you may put a card from your discard pile on top of your deck.",
  :power 1}
 {:set :crisis-1,
  :name "Magic ",
  :type :super-power,
  :cost 5,
  :victory 1,
  :copies 2,
  :text
  "Choose a card you played this turn. Play it again this turn, and then destroy it. (Its effects remain.)"}
 {:set :crisis-1,
  :name "Power of the Red",
  :type :super-power,
  :cost 4,
  :victory 1,
  :copies 3,
  :text "Discard any number of cards, then draw that many cards."}
 {:set :crisis-1,
  :name "Bo Staff",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "+3 Power if there are one or more Villains in the Line-Up. Otherwise, +2 Power.",
  :power "*"}
 {:set :crisis-1,
  :name "Magician's Corset",
  :type :equipment,
  :cost 5,
  :victory 1,
  :copies 1,
  :text
  "Choose any number of players. Each of those players draws a card. +1 Power for each player who draws a card this way (including you).",
  :power "*"}
 {:set :crisis-1,
  :name "Quiver of Arrows",
  :type :equipment,
  :cost 1,
  :victory 1,
  :copies 2,
  :text
  "Choose any number of players. Each of those players reveals the top card of his deck and may discard it."}
 {:set :crisis-1,
  :name "Signature Trenchcoat",
  :type :equipment,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may gain a card with cost 5 or less from the destroyed pile. IF you choose not to,  +2 Power.",
  :power "*"}
 {:set :crisis-1,
  :name "House of Mystery",
  :type :location,
  :cost 4,
  :victory 1,
  :copies 1,
  :ongoing
  "The first time you play a card with cost 5 or greater during each of your turns, draw a card."}
 {:set :crisis-1,
  :name "The Rot",
  :type :location,
  :cost 4,
  :victory 1,
  :copies 1,
  :ongoing
  "The first time you play a Weakness or Vulnerability during each of your turns, draw a card."}
 {:set :crisis-1,
  :name "Ra's Al Ghul (IM)",
  :type :super-villain,
  :cost 9,
  :victory 4,
  :text
  "At the end of your turn, put this card onto the bottom of its owner's deck before drawing a new hand.",
  :ongoing
  "At the end of your turn, if you did not buy or gain a card from the Line-Up, add the top card of the main deck to the Line-Up.",
  :power 4}
 {:set :crisis-1,
  :name "Atrocitus (IM)",
  :type :super-villain,
  :cost 11,
  :victory 5,
  :text "Destroy up to three cards in your discard pile.",
  :attack
  "Each player puts his hand under his Super Hero. If you avoid this Attack, put a random card from your hand under your Super Hero. When this Villain is defeated, discard those cards.",
  :power 3}
 {:set :crisis-1,
  :name "Black Manta (IM)",
  :type :super-villain,
  :cost "10*",
  :victory 5,
  :text
  "You may gain all Equipment in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power.",
  :attack
  "Each player puts all Equipment from his hand into the Line-Up.",
  :ongoing
  "Black Manta costs 3 more to defeat for each Equipment in the Line-Up.",
  :power "*"}
 {:set :crisis-1,
  :name "Brainiac (IM)",
  :type :super-villain,
  :cost 12,
  :victory 6,
  :text
  "Each foe reveals two random cards from his hand. Play one revealed non-Location card from each foe's hand.",
  :attack
  "Each player discards a random card, and then destroys the top X cards of the main deck, where X is the cost of that card."}
 {:set :crisis-1,
  :name "Captain Cold (IM)",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text
  "An additional +1 Power for each foe with a Hero in his discard pile.",
  :attack
  "Each player flips his Super Hero face down. When this Villain is defeated, each player may discard a random card. If you do, flip your Super Hero face up.",
  :power 4}
 {:set :crisis-1,
  :name "Darkseid (IM)",
  :type :super-villain,
  :cost "10*",
  :victory 5,
  :text
  "You may gain all Super Powers in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power.",
  :attack
  "Each player puts all Super Powers from his hand into the Line-Up.",
  :ongoing
  "Darkseid costs 3 more to defeat for each Super Power in the Line-Up.",
  :power "*"}
 {:set :crisis-1,
  :name "Deathstroke (IM)",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text
  "You may gain a Hero and Villain from the Line-Up. If you choose not to, +4 Power.",
  :attack
  "Each player destroys a Hero, Super Power, and Equipment in his hand.",
  :power "*"}
 {:set :crisis-1,
  :name "Hades (IM)",
  :type :super-villain,
  :cost 12,
  :victory 6,
  :text
  "Gain a card from the destroyed pile and put it into your hand.",
  :attack
  "Each player puts the top two cards of the main deck into the Line-Up. Gain Weaknesses equal to the cost of one of those cards."}
 {:set :crisis-1,
  :name "The Joker (IM)",
  :type :super-villain,
  :cost 10,
  :victory 5,
  :text
  "Each foe chooses: They discard two random cards or you draw two cards.",
  :attack
  "Each player puts a random card from his hand into the discard pile of the player on his left. If the card has cost 0, repeat this process.",
  :power 2}
 {:set :crisis-1,
  :name "Lex Luthor (IM)",
  :type :super-villain,
  :cost 11,
  :victory 5,
  :text "Draw four cards.",
  :attack
  "This Attack can't be avoided. Each player gains X Weaknesses, where X is equal to the highest VP value among cards in his hand. (* = 3.)"}
 {:set :crisis-1,
  :name "Parallax (IM)",
  :type :super-villain,
  :cost 13,
  :victory 6,
  :text "Draw a card. Double your Power this turn.",
  :attack
  "Each player reveals his hand and discards all cards with cost 3 or less."}
 {:set :crisis-1,
  :name "Sinestro (IM)",
  :type :super-villain,
  :cost "10*",
  :victory 5,
  :text
  "You may gain all Heroes in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power.",
  :attack
  "Each player puts all Heroes from his hand into the Line-Up.",
  :ongoing
  "Sinestro costs 3 more to defeat for each Hero in the Line-Up.",
  :power "*"}
 {:set :crisis-1,
  :name "Crisis Anti-Monitor (IM)",
  :type :super-villain,
  :cost "13*",
  :ongoing
  "Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."}
 {:set :crisis-1,
  :name "The Flash (Crisis)",
  :type :super-hero,
  :text
  "You go first. Once during each of your turns, if you have played eight or more cards, choose a player. That player draws a card."}
 {:set :crisis-1,
  :name "Superman (Crisis)",
  :type :super-hero,
  :text
  "Once during each of your turns, if you play 7 or greater cost worth of Super Powers, choose a player. That player draws a card."}
 {:set :crisis-1,
  :name "Batman (Crisis)",
  :type :super-hero,
  :text
  "Once during each of your turns, if you have played two or more Equipment, you may choose a player. That player discards his hand, and then draws one fewer cards than he discarded."}
 {:set :crisis-1,
  :name "Wonder Woman (Crisis)",
  :type :super-hero,
  :text
  "When you buy or gain a Villain from the Line-Up, choose a player. That player may destroy a Vulnerability or Weakness in his hand or discard pile."}
 {:set :crisis-1,
  :name "Green Lantern (Crisis)",
  :type :super-hero,
  :text
  "When any player is Attacked, you may discard three cards. If you do, that player avoids the Attack."}
 {:set :crisis-1,
  :name "Aquaman (Crisis)",
  :type :super-hero,
  :text
  "When you buy or gain a card with cost 5 or less during your turn, you may place it into any player's discard pile."}
 {:set :crisis-1,
  :name "Cyborg (Crisis)",
  :type :super-hero,
  :text
  "Once during each of your turns, if you have played a Super Power and Equipment, you may put a card with cost 1 or greater from any player's discard pile on the bottom of the main deck."}
 {:set :crisis-1,
  :name "Martian Manhunter (Crisis)",
  :type :super-hero,
  :text
  "Once during each of your turns, if you have played two or more Heroes, choose a player. That player may put a Hero from his discard pile on top of his deck."}
 {:set :crisis-1,
  :name "Animal Man",
  :type :super-hero,
  :text
  "During each of your turns, if you have played three or more different card types, +2 Power.",
  :power "*"}
 {:set :crisis-1,
  :name "Constantine",
  :type :super-hero,
  :text
  "Once during each of your turns, reveal the top card of your deck. If its cost is 1 or greater, you may play it and it has +1 Power. If you do, destroy it at the end of your turn.",
  :power "*"}
 {:set :crisis-1,
  :name "Green Arrow",
  :type :super-hero,
  :text
  "Once during each of your turns, you may discard a Punch card. If you do, you pay 2 less to defeat Villains and Super-Villains this turn."}
 {:set :crisis-1,
  :name "Robin",
  :type :super-hero,
  :text
  "The first time a card tells you to draw a card during each of your turns, you may instead put an Equipment from your discard pile into your hand."}
 {:set :crisis-1,
  :name "Swamp Thing",
  :type :super-hero,
  :text
  "During your turn, you are considered to control each Location in the Line-Up and in play."}
 {:set :crisis-1,
  :name "Zatanna Zatara",
  :type :super-hero,
  :text
  "Once during each of your turns, you may put a card with cost 1 or greater from your hand on the bottom of your deck. If you do, draw a card."}
 {:set :crisis-1,
  :name "A Death in the Family",
  :type :crisis,
  :to-beat "Each player must destroy a Defense card in his hand.",
  :ongoing
  "At the start of your turn, destroy a Hero in the Line-Up. If there are none to destroy, add the top card of the main deck to the Line-Up."}
 {:set :crisis-1,
  :name "Alternate Reality",
  :type :crisis,
  :to-beat
  "Your team must destroy 12 cost worth of Heroes in their discard piles.",
  :ongoing
  "At the start of your turn, destroy all Heroes in your hand. Gain that many random Villains from the destroyed pile and put them into your hand."}
 {:set :crisis-1,
  :name "Arkham Breakout",
  :type :crisis,
  :to-beat
  "Each player must secretly place a card from his hand face down, and then discard them. If each card has a different name, remove this Crisis. (This card starts the game on top of the Crisis stack.)",
  :ongoing
  "When a Villain with an Attack enters the Line-Up, treat it as if it were a First Appearance -- Attack against all players."}
 {:set :crisis-1,
  :name "Atlantis Attacks",
  :type :crisis,
  :to-beat
  "Each player must discard three non-Starter cards with different card types.",
  :ongoing
  "At the start of your turn, resolve all Villain Attacks in the Line-Up against you. If there are no Attacks, add the top card of the main deck to the Line-Up."}
 {:set :crisis-1,
  :name "Collapsing Parallel Worlds",
  :type :crisis,
  :to-beat
  "Remove this Crisis when there are 10 cards under this card. Then one player destroys his deck and discard pile. The cards under this Crisis become his new deck.",
  :ongoing
  "Each player places all cards he buys or gains from the Line-Up under this card (including Villains)."}
 {:set :crisis-1,
  :name "Dimension Shift",
  :type :crisis,
  :text "Put the top three cards of the main deck into the Line-Up.",
  :to-beat
  "Each player reveals the top card of his deck. IF any player reveals a card with cost exactly 2, remove this Crisis.",
  :ongoing "All cards in the Line-Up cost 2 more to buy."}
 {:set :crisis-1,
  :name "Electromagnetic Pulse",
  :type :crisis,
  :text "Put the top three cards of the main deck into the Line-Up.",
  :to-beat "Each player must destroy an Equipment in his hand.",
  :ongoing
  "Equipment cards lose all text, and have \"+1 Power\" instead."}
 {:set :crisis-1,
  :name "Final Countdown",
  :type :crisis,
  :to-beat
  "Your team must destroy cards in the Line-Up with cost 7, 6, 5, 4, 3, and 2.",
  :ongoing
  "At the start of your turn, put a card with cost 1 or greater from your hand into the Line-Up."}
 {:set :crisis-1,
  :name "Identity Crisis",
  :type :crisis,
  :text
  "Each player flips his Super Hero face down. Players cannot avoid this Super-Villain's First Appearance -- Attack.",
  :to-beat
  "Each player must discard +6 Power worth of cards from his hand. Then flip your Super Hero face up."}
 {:set :crisis-1,
  :name "Kryptonite Meteor",
  :type :crisis,
  :text "Put the top three cards of the main deck into the Line-Up.",
  :to-beat "Each player must destroy a Super Power in his hand.",
  :ongoing
  "Super Power cards lose all text, and have \"+1 Power\" instead."}
 {:set :crisis-1,
  :name "Legion of Doom",
  :type :crisis,
  :text "Put the top three cards of the main deck into the Line-Up.",
  :to-beat "Each player must destroy a Hero in his hand.",
  :ongoing "Hero cards lose all text, and have \"+1 Power\" instead."}
 {:set :crisis-1,
  :name "Rise of the Rot",
  :type :crisis,
  :to-beat
  "Each player must discard a Weakness card. Also remove this Crisis if the Weakness stack runs out.",
  :ongoing
  "At the start of your turn, gain a Weakness unless you reveal one from your hand."}
 {:set :crisis-1,
  :name "Untouchable Villain",
  :type :crisis,
  :to-beat
  "Each player must destroy 8 cost worth of cards in his hand. (Destroy cards only with cost 1 or greater.)",
  :ongoing
  "At the start of your turn, put the top card of the main deck into the Line-Up. If it is not a Villain, discard a card."}
 {:set :crisis-1,
  :name "Wave of Terror",
  :type :crisis,
  :to-beat
  "Each player discards a random card from his hand. Remove this Crisis if each of those cards has cost 1 or greater.",
  :ongoing
  "At the start of your turn, discard all cards that share the lowest cost in your hand."}
 {:set :crisis-1,
  :name "World Domination",
  :type :crisis,
  :to-beat
  "Each player must destroy the top card of his deck. Remove this Crisis only if each of those cards has cost 1 or greater.",
  :ongoing
  "At the start of your turn, resolve the current Super-Villain's First Appearance -- Attack against you. If it doesn't have one, discard a random card instead."}
 {:set :crossover-5,
  :name "Captain Boomerang Jr.",
  :type :hero,
  :cost 5,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Each player puts a card with VP value 1 or less from their discard pile on top of their deck.",
  :power 3}
 {:set :crossover-5,
  :name "Iris West",
  :type :hero,
  :cost 2,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Look at the top of each player's deck.",
  :power 1}
 {:set :crossover-5,
  :name "James Jesse",
  :type :hero,
  :cost 4,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. If you Teamworked this turn, draw a card.",
  :power 2}
 {:set :crossover-5,
  :name "Patty Spivot",
  :type :hero,
  :cost 3,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Teamwork a foe. If you draw or have drawn one or more cards this turn, Teamwork a foe."}
 {:set :crossover-5,
  :name "Pied Piper",
  :type :hero,
  :cost 6,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 2 VPs. Teamwork a foe.",
  :defense
  "You may reveal this card from your hand to avoid an Attack. If you do, gain 1 VP."}
 {:set :crossover-5,
  :name "Abra Kadabra",
  :type :villain,
  :cost 7,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 4 VPs. Play the top card of the main deck. At end of turn, return it or pay 2 VPs to gain it."}
 {:set :crossover-5,
  :name "Dr. Alchemy",
  :type :villain,
  :cost 2,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Gain a card from the Line-Up and put it into your hand. Then, put a card with the same cost from your hand into the Line-Up."}
 {:set :crossover-5,
  :name "Girder",
  :type :villain,
  :cost 4,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 1 VP.",
  :defense
  "You may discard this card to avoid an Attack. If you do, draw a card and gain 1 VP.",
  :power 2}
 {:set :crossover-5,
  :name "Magenta",
  :type :villain,
  :cost 5,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. You may pay 1 VP. If you do, gain an Equipment from the Line-Up and put it into your hand.",
  :power 2}
 {:set :crossover-5,
  :name "Tar Pit",
  :type :villain,
  :cost 3,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Draw a card. You may pay 1 VP. If you do, destroy a card in your hand and/or discard pile."}
 {:set :crossover-5,
  :name "The Top",
  :type :villain,
  :cost 6,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 2 VPs. Teamwork a foe.",
  :attack "Steal 1 VP from each foe."}
 {:set :crossover-5,
  :name "Engulfing Flames",
  :type :super-power,
  :cost 5,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Destroy a card in your hand. If that card has cost 1 or greater, draw two cards.",
  :power 2}
 {:set :crossover-5,
  :name "Lightning Strike",
  :type :super-power,
  :cost 6,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 2 VPs. Draw two cards. +1 Power for each card you discard or have discarded this turn.",
  :power "*"}
 {:set :crossover-5,
  :name "Mirror Images",
  :type :super-power,
  :cost 5,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Play the top card of your deck. You may put a card with that cost from your discard pile into your hand."}
 {:set :crossover-5,
  :name "Phasing",
  :type :super-power,
  :cost 7,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 3 VPs. +1 Power for each card in your hand.",
  :power "*"}
 {:set :crossover-5,
  :name "Tornado",
  :type :super-power,
  :cost 4,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 1 VP. Draw a card.",
  :attack
  "Look at that foe's hand. Put a card from their hand on top of their deck."}
 {:set :crossover-5,
  :name "Air-Walk Shoes",
  :type :equipment,
  :cost 3,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Put a card with VP value 1 or less from your discard pile into your hand."}
 {:set :crossover-5,
  :name "Back of Tricks",
  :type :equipment,
  :cost 6,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 2 VPs. Pay X VPs: +X Power and draw X cards."}
 {:set :crossover-5,
  :name "Cold Gun",
  :type :equipment,
  :cost 3,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Each time you Teamwork this turn, gain 1 VP.",
  :power 2}
 {:set :crossover-5,
  :name "Loot!",
  :type :equipment,
  :cost 4,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 7 VPs. If you do not own this card, steal 2 VPs from its owner."}
 {:set :crossover-5,
  :name "Mirror Gun",
  :type :equipment,
  :cost 4,
  :victory 0,
  :copies 1,
  :text
  "When you buy or gain this card, gain 1 VP. Put a copy of each card you have already played this turn from your discard pile into your hand."}
 {:set :crossover-5,
  :name "Iron Heights",
  :type :location,
  :cost 4,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 1 VP.",
  :ongoing
  "Defense: You may give the attacker 1 VP. If you do, avoid that Attack."}
 {:set :crossover-5,
  :name "Rogues Safe House",
  :type :location,
  :cost 4,
  :victory 0,
  :copies 1,
  :text "When you buy or gain this card, gain 1 VP.",
  :ongoing "Each time you Teamwork a foe, +1 Power."}
 {:set :crossover-5,
  :name "The Flash",
  :type :super-hero,
  :cost 8,
  :victory 0,
  :text
  "When you defeat The Flash, remove him from the game, gain 6 VPs, and you may destroy a Punch in play.",
  :ongoing "Super-Villains lose their Teamwork abilities."}
 {:set :crossover-5,
  :name "Jesse Quick",
  :type :super-hero,
  :cost 9,
  :victory 5,
  :text
  "Look at the top card of each foe's deck. FIRST APPEARANCE - ATTACK: Each player destroys the top card of their deck. (Teamwork abilities are now active.)",
  :power 3}
 {:set :crossover-5,
  :name "Jay Garrick",
  :type :super-hero,
  :cost 10,
  :victory 5,
  :text
  "Draw two cards and gain 1 VP. FIRST APPEARANCE - ATTACK: Each player passes a card from their hand to the discard pile of the player on their left. If you pass a card with cost 1 or greater, gain 1 VP."}
 {:set :crossover-5,
  :name "Hawkman",
  :type :super-hero,
  :cost 11,
  :victory 6,
  :text
  "Gain 2 VPs. FIRST APPEARANCE - ATTACK: Each player discards cards until they have discarded 5 power worth of cards. If your hand empties this way, gain 1 VP.",
  :power 3}
 {:set :crossover-5,
  :name "Max Mercury",
  :type :super-hero,
  :cost 12,
  :victory 6,
  :text
  "Draw two cards. Teamwork a foe. FIRST APPEARANCE - ATTACK: Each player adds the top card of the main deck to the Line-Up. Lose a number of VPs equal to the VP calue of that card (*=3)."}
 {:set :crossover-5,
  :name "Wally West",
  :type :super-hero,
  :cost 13,
  :victory 6,
  :text "Draw three cards.",
  :ongoing
  "Wally West cannot be defeated unless you have played nine or more cards this turn. FIRST APPEARANCE - ATTACK: Each player gains two cards with cost 0 from the destroyed pile."}
 {:set :crossover-5,
  :name "Bart Allen",
  :type :super-hero,
  :cost 14,
  :victory 7,
  :text
  "Gain two cards from the Line-Up and put them into your hand. Then, refill the Line-Up. FIRST APPEARANCE - ATTACK: Each player reveals their hand and gains a Weakness for each different card with VP Value 1 or greater revealed this way."}
 {:set :crossover-5,
  :name "The Flash [Return from the Speed Force]",
  :type :super-hero,
  :cost 15,
  :victory 7,
  :ongoing
  "At the start of your turn, lose 1 VP. Each time you play a card from your hand, discard a card. Players may Teamwork any number of different foes."}
 {:set :crossover-5,
  :name "Captain Cold",
  :type :super-villain,
  :text
  "The first time you buy a card with cost 3 or less from the Line-Up during each of your turns, you may Teamwork a foe. Teamwork: That foe gains 1 VP."}
 {:set :crossover-5,
  :name "Heatwave",
  :type :super-villain,
  :text
  "The first time you destroy a card with cost 2 or greater during each of your turns, gain the top card of the main deck and put it on top of your deck. Teamwork: That foe gains 1 VP."}
 {:set :crossover-5,
  :name "Mirror Master",
  :type :super-villain,
  :text
  "Each time you play a Hero, Villain, or Equipment, +1 Power if you have played another card wth that cost this turn. Teamwork: That foe gains 1 VP.",
  :power "*"}
 {:set :crossover-5,
  :name "Trickster",
  :type :super-villain,
  :text
  "Each time you would shuffle your discard pile, you may leave one of those cards in your discard pile. Teamwork: That foe gains 1 VP."}
 {:set :crossover-5,
  :name "Weather Wizard",
  :type :super-villain,
  :text
  "Discard two cards: Draw a card. Teamwork: That foe gains 1 VP."}
 {:set :crossover-5,
  :name "Golden Glider",
  :type :super-villain,
  :text
  "Each time you play a card you don't own, draw a card. Teamwork: That foe gains 1 VP."}
 {:set :forever-evil,
  :name "Amanda Waller",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a card in your hand or discard pile. If it's a Villain, +Power equal to its cost. If you choose not to, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Catwoman",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 2,
  :attack "Steal 1 VP from that foe.",
  :power 1}
 {:set :forever-evil,
  :name "Commissioner Gordon",
  :type :hero,
  :cost 2,
  :victory 1,
  :copies 3,
  :defense
  "You may discard this card to avoid an Attack. If you do, gain 1 VP.",
  :power 1}
 {:set :forever-evil,
  :name "Dr. Light",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 2,
  :text "Draw a card.",
  :attack
  "That foe puts a Location he controls into his discard pile."}
 {:set :forever-evil,
  :name "Element Woman",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "While you own or are playing this card, it is also a Super Power, Equipment, and Villain.",
  :power 2}
 {:set :forever-evil,
  :name "Firestorm",
  :type :hero,
  :cost 6,
  :victory 2,
  :copies 1,
  :text
  "Put the top card of your deck on your Super-Villain. This card has the game text of each card on your Super-Villain this turn. (At end of game, destroy those cards.)"}
 {:set :forever-evil,
  :name "Pandora",
  :type :hero,
  :cost 7,
  :victory 2,
  :copies 1,
  :text
  "Add the top card of the Main Deck to the Line-Up. +1 Power for each different cost among cards in the Line-Up.",
  :power "*"}
 {:set :forever-evil,
  :name "Phantom Stranger",
  :type :hero,
  :cost 5,
  :victory "10*",
  :copies 1,
  :text
  "You may destroy a card in your hand and you may destroy a card in your discard pile. At end of game, this card is worth 1 fewer VP for each card with cost 0 in your deck. (Minimum 0)"}
 {:set :forever-evil,
  :name "Power Girl",
  :type :hero,
  :cost 5,
  :victory 2,
  :copies 3,
  :power 3}
 {:set :forever-evil,
  :name "Stargirl",
  :type :hero,
  :cost 4,
  :victory 1,
  :copies 2,
  :defense
  "You may discard this card to avoid an Attack. If you do, draw a card and put a card with cost 1 or greater from the destroyed pile on the bottom of the main deck.",
  :power 2}
 {:set :forever-evil,
  :name "Steel",
  :type :hero,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a non-Hero card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."}
 {:set :forever-evil,
  :name "Steve Trevor",
  :type :hero,
  :cost 1,
  :victory 0,
  :copies 2,
  :text
  "When you destroy this card in any zone, draw two cards, and then discard a card."}
 {:set :forever-evil,
  :name "Vibe",
  :type :hero,
  :cost 2,
  :victory 1,
  :copies 2,
  :text
  "You may put a Hero or Super Power with cost 5 or less from your discard pile on top of your deck.",
  :power 1}
 {:set :forever-evil,
  :name "Atomica",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a non-Villain card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."}
 {:set :forever-evil,
  :name "The Blight",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a card in your hand or discard pile. If it's a Super Power, +Power equal to its cost. If you choose not to, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Deathstorm",
  :type :villain,
  :cost 4,
  :victory "10*",
  :copies 1,
  :text
  "You may destroy a card in your hand. At end of game, this card is worth 1 fewer VP for each card in excess of 20 in your deck. (Minimum 0.)"}
 {:set :forever-evil,
  :name "Despero",
  :type :villain,
  :cost 6,
  :victory 2,
  :copies 1,
  :text "Draw two cards.",
  :attack "That foe discards a card with cost 1 or greater."}
 {:set :forever-evil,
  :name "Emperor Penguin",
  :type :villain,
  :cost 1,
  :victory 0,
  :copies 2,
  :text "When you destroy this card in any zone, gain 2 VPs."}
 {:set :forever-evil,
  :name "Giganta",
  :type :villain,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "+4 Power if there there are no cards with cost 3 or less in your discard pile. Otherwise, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Grid",
  :type :villain,
  :cost 2,
  :victory 1,
  :copies 2,
  :text
  "You may put a Villain or Equipment with cost 5 or less from your discard pile on top of your deck.",
  :power 1}
 {:set :forever-evil,
  :name "Johnny Quick",
  :type :villain,
  :cost 2,
  :victory 1,
  :copies 2,
  :text "Draw a card."}
 {:set :forever-evil,
  :name "Man-Bat",
  :type :villain,
  :cost 3,
  :victory 1,
  :copies 2,
  :defense
  "You may discard this card to avoid an Attack. If you do, steal 1 VP from the attacker.",
  :power 2}
 {:set :forever-evil,
  :name "Owlman",
  :type :villain,
  :cost 6,
  :victory 2,
  :copies 1,
  :text
  "You may destroy an Equipment in the Line-Up. +1 Power for each different Equipment in the destroyed pile.",
  :power "*"}
 {:set :forever-evil,
  :name "Power Ring",
  :type :villain,
  :cost 6,
  :victory 2,
  :copies 1,
  :text
  "You may destroy a Hero in the Line-Up. +1 Power for each different Hero in the destroyed pile.",
  :power "*"}
 {:set :forever-evil,
  :name "Royal Flush Gang",
  :type :villain,
  :cost 5,
  :victory 0,
  :copies 5,
  :text
  "Draw two cards, and then discard two cards. Gain 1 VP for each other Royal Flush Gang you play or have played this turn."}
 {:set :forever-evil,
  :name "Superwoman",
  :type :villain,
  :cost 7,
  :victory 3,
  :copies 1,
  :text
  "You may destroy a Villain in the Line-Up. +1 Power for each different Villain in the destroyed pile.",
  :power "*"}
 {:set :forever-evil,
  :name "Ultraman",
  :type :villain,
  :cost 8,
  :victory 3,
  :copies 1,
  :text
  "You may destroy a card in the Line-Up. +1 Power for each different Super Power in the destroyed pile.",
  :power "*"}
 {:set :forever-evil,
  :name "Bizarro Power",
  :type :super-power,
  :cost 6,
  :victory -1,
  :copies 2,
  :text "Gain a Weakness.",
  :attack "Each foe gains a Weakness.",
  :power 4}
 {:set :forever-evil,
  :name "Constructs of Fear",
  :type :super-power,
  :cost 7,
  :victory 2,
  :copies 2,
  :attack "Each foe discards two cards.",
  :power 3}
 {:set :forever-evil,
  :name "Expert Marksman",
  :type :super-power,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a non-Super Power card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."}
 {:set :forever-evil,
  :name "Giant Growth",
  :type :super-power,
  :cost 2,
  :victory 1,
  :copies 3,
  :power 2}
 {:set :forever-evil,
  :name "Insanity",
  :type :super-power,
  :cost 2,
  :victory 1,
  :copies 3,
  :text
  "If this is the first card you play this turn, each player passes a card in his hand to the hand of the player on his left.",
  :defense
  "You may discard this card to avoid an Attack. If you do, you may put a card from your hand or discard pile into the attacker's hand."}
 {:set :forever-evil,
  :name "Invulnerable",
  :type :super-power,
  :cost 3,
  :victory 1,
  :copies 3,
  :defense
  "When you are attacked, you may reveal this card from your hand. If you do, you may discard it or destroy a Vulnerability in your hand or discard pile to avoid an Attack.",
  :power 1}
 {:set :forever-evil,
  :name "Power Drain",
  :type :super-power,
  :cost 4,
  :victory 1,
  :copies 2,
  :attack
  "That foe reveals his hand. Choose one card revealed this way to be discarded.",
  :power 2}
 {:set :forever-evil,
  :name "Super Intellect",
  :type :super-power,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a card in your hand or discard pile. If it's an Equipment, +Power equal to its cost. If you choose not to, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Transmutation",
  :type :super-power,
  :cost 5,
  :victory 1,
  :copies 3,
  :text
  "Destroy a card in your hand or discard pile and gain 1 VP. You may gain a card from the Line-Up of equal or lesser cost than the card destroyed this way."}
 {:set :forever-evil,
  :name "Ultra Strength",
  :type :super-power,
  :cost 9,
  :victory 3,
  :copies 1,
  :text "Draw two cards.",
  :power 3}
 {:set :forever-evil,
  :name "Word of Power",
  :type :super-power,
  :cost 1,
  :victory 0,
  :copies 2,
  :text
  "When you destroy this card in any zone, you pay 4 less to defeat Super Heroes this turn."}
 {:set :forever-evil,
  :name "Broadsword",
  :type :equipment,
  :cost 6,
  :victory 2,
  :copies 2,
  :attack
  "Destroy a card with cost 1, 2, or 3 in that foe's discard pile.",
  :power 2}
 {:set :forever-evil,
  :name "Cold Gun",
  :type :equipment,
  :cost 2,
  :victory 1,
  :copies 3,
  :text
  "You may put a Frozen token on a card in the Line-Up. If you do, remove it at the start of your next turn.",
  :power 1}
 {:set :forever-evil,
  :name "Cosmic Staff",
  :type :equipment,
  :cost 5,
  :victory 1,
  :copies 3,
  :defense
  "You may discard this card to avoid an Attack. If you do, gain the bottom card of the main deck.",
  :power 2}
 {:set :forever-evil,
  :name "Firestorm Matrix",
  :type :equipment,
  :cost 7,
  :victory 2,
  :copies 2,
  :text
  "Play the top card of your deck. If its cost is 5 or less, you may destroy this card. If you do, leave the card you played in front of you for the rest of the game and it has \"",
  :ongoing
  "You may play this card once during each of your turns. (At end of game, destroy it.)\""}
 {:set :forever-evil,
  :name "Mallet",
  :type :equipment,
  :cost 4,
  :victory 1,
  :copies 3,
  :text
  "Reveal the top card of your deck. Draw it or pass it to any player's discard pile."}
 {:set :forever-evil,
  :name "Man-Bat Serum",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 3,
  :text
  "+Power equal to your VPs. If you have 5 or more VPs, destroy this card at the end of your turn.",
  :power "*"}
 {:set :forever-evil,
  :name "Pandora's Box",
  :type :equipment,
  :cost 2,
  :victory 1,
  :copies 2,
  :text
  "Reveal the top card of the main deck. Add cards from the main deck to the Line-Up equal to the revealed card's cost."}
 {:set :forever-evil,
  :name "Power Armor",
  :type :equipment,
  :cost 8,
  :victory 3,
  :copies 1,
  :defense
  "You may reveal this card from your hand to avoid an Attack. If you do, you may destroy a card in your hand or discard pile.",
  :power 3}
 {:set :forever-evil,
  :name "Secret Society Communicator",
  :type :equipment,
  :cost 4,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a card in your hand or discard pile. If it's a Hero, +Power equal to its cost. If you choose not to, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Sledgehammer",
  :type :equipment,
  :cost 3,
  :victory 1,
  :copies 2,
  :text
  "You may destroy a non-Equipment card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."}
 {:set :forever-evil,
  :name "Venom Injector",
  :type :equipment,
  :cost 1,
  :victory 0,
  :copies 2,
  :text "When you destroy this card in any zone, +2 Power.",
  :power "*"}
 {:set :forever-evil,
  :name "Belle Reve",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing "+1 Power for each Villain you play"}
 {:set :forever-evil,
  :name "Blackgate Prison",
  :type :location,
  :cost 4,
  :victory 1,
  :copies 1,
  :ongoing
  "Once during each of your turns, reveal the top card of your deck. If it's a Vulnerability or Weakness, destroy it and gain 1 VP."}
 {:set :forever-evil,
  :name "Central City",
  :type :location,
  :cost 4,
  :victory 1,
  :copies 1,
  :ongoing "+1 Power for each non-Kick Super Power you play."}
 {:set :forever-evil,
  :name "Earth-3",
  :type :location,
  :cost 6,
  :victory 1,
  :copies 1,
  :ongoing
  "Once during each of your turns, reveal the top card of your deck. If it's a Punch, destroy it and gain 1 VP."}
 {:set :forever-evil,
  :name "Happy Harbor",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing "+1 Power for each Hero you play."}
 {:set :forever-evil,
  :name "S.T.A.R. Labs",
  :type :location,
  :cost 5,
  :victory 1,
  :copies 1,
  :ongoing "+1 Power for each Equipment you play."}
 {:set :forever-evil,
  :name "The Flash",
  :type :super-hero,
  :cost 8,
  :victory 4,
  :text
  "Draw three cards, and then discard a card. (This card starts the game on top of the Super Hero stack)."}
 {:set :forever-evil,
  :name "Aquaman",
  :type :super-hero,
  :cost 11,
  :victory 6,
  :text
  "You may put up to three cards from your discard pile on top of your deck. If you choose not to, +3 Power.",
  :attack
  "Each player puts four cards with cost 0 from his discard pile on top of his deck. If you put none there, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Batman",
  :type :super-hero,
  :cost 11,
  :victory 6,
  :text
  "You may play up to three Equipment with cost 6 or less from the destroyed pile, and then put them on the bottom of the main deck. If you chose not to, +3 Power.",
  :attack
  "Each player destroys an Equipment in his hand or discard pile. If you cannot, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Constantine",
  :type :super-hero,
  :cost 10,
  :victory 5,
  :text
  "Reveal the top three cards of your deck. Draw one, destroy one, and put one on top of your deck. Gain VPs equal to the destroyed card's VP value.",
  :attack
  "Each player loses 3 VPs. If you have none to lose, gain a Weakness."}
 {:set :forever-evil,
  :name "Cyborg",
  :type :super-hero,
  :cost 10,
  :victory 5,
  :text
  "+2 Power for each Super Power and Equipment you play or have played.",
  :attack
  "Each player discards a Super Power and an Equipment. If you discard no cards, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Green Arrow",
  :type :super-hero,
  :cost 9,
  :victory 5,
  :text
  "When you play this card, leave it in front of you for the rest of the game.",
  :attack
  "Each player discards two Punch cards. For each Punch you fail to discard, gain a Weakness.",
  :ongoing "Punch cards you play have an additional +1 Power."}
 {:set :forever-evil,
  :name "Green Lantern",
  :type :super-hero,
  :cost 11,
  :victory 6,
  :text
  "You may play up to three Heroes with cost 6 or less from the destroyed pile, and then put them on the  bottom of the main deck. If you choose not to, +3 Power.",
  :attack
  "Each player destroys a Hero in his hand or discard pile. If you cannot, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Martian Manhunter",
  :type :super-hero,
  :cost 12,
  :victory 6,
  :text
  "+2 Power for each Hero and Villain you play or have played this turn.",
  :attack
  "Each player discards a Hero and a Villain. If you discard no cards, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Shazam!",
  :type :super-hero,
  :cost 12,
  :victory 6,
  :text
  "Gain the top two cards of the main deck, play them, and then destroy one of them. (Its effects remain.)",
  :attack
  "Each player puts a card with cost 5 or greater from his hand or discard pile on the bottom of the main deck. If you cannot, gain a Weakness."}
 {:set :forever-evil,
  :name "Superman",
  :type :super-hero,
  :cost 13,
  :victory 6,
  :text
  "You may play up to three Super Powers from the destroyed pile, and then put them on the bottom of the main deck. If you choose not to, +4 Power.",
  :attack
  "Each player destroys a Super Power in his hand or discard pile. If you cannot, gain two Weakness cards.",
  :power "*"}
 {:set :forever-evil,
  :name "Swamp Thing",
  :type :super-hero,
  :cost 9,
  :victory 5,
  :text "+2 Power for each Location in play.",
  :attack
  "Each player puts a Location he controls into his discard pile. If you cannot, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Wonder Woman",
  :type :super-hero,
  :cost 11,
  :victory 6,
  :text
  "You may play up to three Villains with cost 6 or less from the destroyed pile, and then put them on the bottom of the main deck. If you choose not to, +3 Power.",
  :attack
  "Each player destroys a Villain in his hand or discard pile. If you cannot, gain a Weakness.",
  :power "*"}
 {:set :forever-evil,
  :name "Bizarro",
  :type :super-villain,
  :text
  "When you destroy a card, gain a Weakness. During your turn, you may put two Weakness cards from your discard pile on top of the Weakness stack. If you do, draw a card."}
 {:set :forever-evil,
  :name "Lex Luthor",
  :type :super-villain,
  :text
  "At the end of your turn, draw an extra card for each Hero you bought or gained during your turn."}
 {:set :forever-evil,
  :name "Deathstroke",
  :type :super-villain,
  :text "+1 Power for each card you destroy during your turn.",
  :power "*"}
 {:set :forever-evil,
  :name "Harley Quinn",
  :type :super-villain,
  :text
  "During each player's turn, the first time you pass a card or discard a card, draw a card."}
 {:set :forever-evil,
  :name "Sinestro",
  :type :super-villain,
  :text
  "When one or more foes fail to avoid an Attack you play, gain 1 VP. The first time you gain VPs during each of your turns, draw a card."}
 {:set :forever-evil,
  :name "Black Manta",
  :type :super-villain,
  :text
  "You may put any cards you buy or gain from the Line-Up on the bottom of your deck."}
 {:set :forever-evil,
  :name "Black Adam",
  :type :super-villain,
  :text
  "The first time you play a Super Power during each of your turns, you may destroy it. If you do, draw a card and gain 1 VP."}
 {:set :forever-evil,
  :name "Bane",
  :type :super-villain,
  :text
  "If the first card you play during each of your turns has cost 1 or greater, you may destroy it. If you do, +2 Power.",
  :power "*"}]
)
