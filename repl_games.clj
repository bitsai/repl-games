(ns repl-games.random)

;; PRNG fns using a settable seed, based from clojure.data.generators
;; https://github.com/clojure/data.generators

(def rnd (atom (java.util.Random. (System/currentTimeMillis))))

(defn set-seed! [seed]
  (.setSeed @rnd seed))

(defn uniform
  (^long []
         (.nextLong @rnd))
  (^long [lo hi]
         (-> (.nextDouble @rnd)
             (* (- hi lo))
             (+ lo)
             (Math/floor)
             (long))))

(defn rand-nth* [coll]
  (nth coll (uniform 0 (count coll))))

(defn shuffle* [coll]
  (let [as (object-array coll)]
    (loop [i (dec (count as))]
      (if (<= 1 i)
        (let [j (uniform 0 (inc i))
              t (aget as i)]
          (aset as i (aget as j))
          (aset as j t)
          (recur (dec i)))
        (into (empty coll) (seq as))))))
(ns repl-games.core
  (:require [repl-games.random :as rand]))

(defn mk-game [mk-game-state seed]
  ;; set PRNG seed
  (rand/set-seed! seed)
  ;; mk-game-state should update :messages and :state
  (mk-game-state {:commands [[:setup seed]]
                  :messages nil
                  :state nil}))

(defn update-game [game cmd-name cmd-fn args]
  ;; iff an updated game is produced ...
  (when-let [g (apply cmd-fn game args)]
    ;; update command log
    (update g :commands conj [cmd-name args])))

(defn replay-commands [cmds cmd-map mk-game-state]
  ;; first command contains data for making new game
  (let [[_ seed] (first cmds)]
    ;; replay commands
    (-> (reduce (fn [game [cmd-name args]]
                  (let [cmd-fn (-> cmd-map cmd-name :fn)]
                    (update-game game cmd-name cmd-fn args)))
                (mk-game mk-game-state seed)
                (rest cmds))
        ;; discard messages during replay
        (assoc :messages nil))))

(defn print-and-reset! [game print-game game-atom]
  (print-game game)
  (-> game
      ;; discard printed messages
      (assoc :messages nil)
      ;; update game atom
      (->> (reset! game-atom)))
  nil)

(defn mk-command! [ns game-atom help-atom cmd-name cmd-spec print-game]
  (swap! help-atom conj [(name cmd-name) (:doc cmd-spec)])
  (intern ns
          (-> cmd-name name symbol)
          (fn [& args]
            (when-let [g (update-game @game-atom cmd-name (:fn cmd-spec) args)]
              (print-and-reset! g print-game game-atom)))))

(defn mk-commands! [ns game-atom help-atom meta-fn-map cmd-map]
  (let [{:keys [mk-game-state print-game]} meta-fn-map]
    ;; create help command
    (swap! help-atom conj ["help" "(help)"])
    (intern ns 'help #(doseq [[x y] @help-atom]
                        (println x y)))
    ;; create setup command
    (swap! help-atom conj ["su" "(setup)"])
    (intern ns 'su #(-> (mk-game mk-game-state (System/currentTimeMillis))
                        (print-and-reset! print-game game-atom)))

    ;; create undo command
    (swap! help-atom conj ["un" "(undo)"])
    (intern ns 'un #(let [cmds (-> @game-atom :commands butlast)]
                      ;; allow undo iff there is at least 1 command to replay
                      (when (-> cmds count pos?)
                        (-> (replay-commands cmds cmd-map mk-game-state)
                            (print-and-reset! print-game game-atom)))))
    ;; create game commands
    (doseq [[cmd-name cmd-spec] cmd-map]
      (mk-command! ns game-atom help-atom cmd-name cmd-spec print-game))))
(ns dcdbg.cards.core)

(def weakness
  {:name "Weakness"
   :cost 0
   :victory -1
   :text "Weakness cards reduce your score at the end of the game."
   :copies 20})

(def kick
  {:name "Kick"
   :type :super-power
   :cost 3
   :victory 1
   :power 2
   :copies 16})

(def vulnerability
  {:name "Vulnerability"
   :type :starter
   :cost 0
   :victory 0
   :copies 16})

(def punch
  {:name "Punch"
   :type :starter
   :cost 0
   :victory 0
   :power 1
   :copies 36})
(ns dcdbg.cards.equipment)

(def base
  [{:name "Aquaman's Trident"
    :cost 3
    :victory 1
    :text "You may put any one card you buy or gain this turn on top of your deck."
    :power 2
    :defense nil
    :copies 3}
   {:name "Batmobile"
    :cost 2
    :victory 1
    :text "If this is the first card you play this turn, discard your hand and draw five cards. Otherwise, +1 Power."
    :power "1?"
    :defense nil
    :copies 3}
   {:name "The Bat-Signal"
    :cost 5
    :victory 1
    :text "Put a Hero from your discard pile into your hand."
    :power 1
    :defense nil
    :copies 3}
   {:name "The Cape and Cowl"
    :cost 4
    :victory 1
    :text nil
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :copies 3}
   {:name "Green Arrow's Bow"
    :cost 4
    :victory 1
    :text "Super-Villains cost you 2 less to defeat this turn."
    :power 2
    :defense nil
    :copies 3}
   {:name "Lasso of Truth"
    :cost 2
    :victory 1
    :text nil
    :power 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card."
    :copies 3}
   {:name "Nth Metal"
    :cost 3
    :victory 1
    :text "Look at the top card of your deck. You may destroy it."
    :power 1
    :defense nil
    :copies 4}
   {:name "Power Ring"
    :cost 3
    :victory 1
    :text "Reveal the top card of your deck. If its cost is 1 or greater, +3 Power. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 3}
   {:name "Utility Belt"
    :cost 5
    :victory "5?"
    :text "At the end of the game, if you have four or more other Equipment in your deck, this card is worth 5 VPs."
    :power 2
    :defense nil
    :copies 3}])

(def crisis1
  [{:name "Bo Staff"
    :cost 3
    :victory 1
    :text "+3 Power if there are one or more Villains in the Line-Up. Otherwise, +2 Power."
    :power "2+"
    :defense nil
    :copies 2}
   {:name "Magician's Corset"
    :cost 5
    :victory 1
    :text "Choose any number of players. Each of those players draws a card. +1 Power for each player who draws a card this way (including you)."
    :power "0+"
    :defense nil
    :copies 1}
   {:name "Quiver of Arrows"
    :cost 1
    :victory 1
    :text "Choose any number of players. Each of those players reveals the top card of his deck and may discard it."
    :power nil
    :defense nil
    :copies 2}
   {:name "Signature Trenchcoat"
    :cost 4
    :victory 1
    :text "You may gain a card with cost 5 or less from the destroyed pile. If you choose not to, +2 Power."
    :power "2?"
    :defense nil
    :copies 2}])

(def crossover1
  [{:name "The Hourglass"
    :cost 4
    :victory 1
    :text "Choose another card with cost 6 or less that you played this turn. At end of turn, put that card into your hand."
    :power nil
    :defense nil
    :copies 1}
   {:name "T-Spheres"
    :cost 6
    :victory 2
    :text "Choose a card name. Reveal the top three cards of your deck. Put all cards with that name into your hand and the rest on top in any order."
    :power 2
    :defense nil
    :copies 1}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :equipment))))
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
(ns dcdbg.cards.location)

(def base
  [{:name "Arkham Asylum"
    :cost 5
    :victory 1
    :ongoing "When you play your first Villain on each of your turns, draw a card."
    :copies 1}
   {:name "The Batcave"
    :cost 5
    :victory 1
    :ongoing "When you play your first Equipment on each of your turns, draw a card."
    :copies 1}
   {:name "Fortress of Solitude"
    :cost 5
    :victory 1
    :ongoing "When you play your first Super Power on each of your turns, draw a card."
    :copies 1}
   {:name "Titans Tower"
    :cost 5
    :victory 1
    :ongoing "When you play your first card with cost 2 or 3 on each of your turns, draw a card."
    :copies 1}
   {:name "The Watchtower"
    :cost 5
    :victory 1
    :ongoing "When you play your first Hero on each of your turns, draw a card."
    :copies 1}])

(def crisis1
  [{:name "House of Mystery"
    :cost 4
    :victory 1
    :ongoing "The first time you play a card with cost 5 or greater during each of your turns, draw a card."
    :copies 1}
   {:name "The Rot"
    :cost 4
    :victory 1
    :ongoing "The first time you play a Weakness or Vulnerability during each of your turns, draw a card."
    :copies 1}])

(def crossover1
  [{:name "Monument Point"
    :cost 6
    :victory 2
    :ongoing "When you play your first Punch during each of your turns, draw a card."
    :copies 1}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :location))))
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

(def crossover1
  [{:name "Girl Power"
    :cost 5
    :victory 1
    :text nil
    :power 2
    :defense "You may reveal this card and discard it or a Punch to avoid an Attack. If you do, draw a card."
    :copies 1}
   {:name "Mystic Bolts"
    :cost 6
    :victory 2
    :text "Put up to two cards each with cost 5 or less and each with a different cost from your discard pile into your hand."
    :power 1
    :defense nil
    :copies 1}])

(def all
  (->> (concat base crisis1 crossover1)
       (map #(assoc % :type :super-power))))
(ns dcdbg.cards.super-villain)

(def crisis1
  [{:name "Ra's Al Ghul (Imp.)"
    :cost 9
    :victory 4
    :text "At the end of your turn, put this card onto the bottom of its owner's deck before drawing a new hand."
    :power 4
    :stack-ongoing "At the end of your turn, if you did not buy or gain a card from the Line-Up, add the top card of the main deck to the Line-Up."
    :first-appearance-attack nil}
   {:name "Crisis Anti-Monitor (Imp.)"
    :cost "13+"
    :victory nil
    :text nil
    :power nil
    :stack-ongoing "Add the top card of the main deck to the Line-Up. Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."
    :first-appearance-attack nil}
   {:name "Atrocitus (Imp.)"
    :cost 11
    :victory 5
    :text "Destroy up to three cards in your discard pile."
    :power 3
    :stack-ongoing nil
    :first-appearance-attack "Each player puts his hand under his Super Hero. If you avoid this Attack, put a random card from your hand under your Super Hero. When this Villain is defeated, discard those cards."}
   {:name "Black Manta (Imp.)"
    :cost "10+"
    :victory 5
    :text "You may gain all Equipment in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Black Manta costs 3 more to defeat for each Equipment in the Line-Up."
    :first-appearance-attack "Each player puts all Equipment from his hand into the Line-Up."}
   {:name "Brainiac (Imp.)"
    :cost 12
    :victory 6
    :text "Each foe reveals two random cards from his hand. Play one revealed non-Location card from each foe's hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player discards a random card, and then destroys the top X cards of the main deck, where X is the cost of that card."}
   {:name "Captain Cold (Imp.)"
    :cost 10
    :victory 5
    :text "An additional +1 Power for each foe with a Hero in his discard pile."
    :power "4+"
    :stack-ongoing nil
    :first-appearance-attack "Each player flips his Super Hero face down. When this Villain is defeated, each player may discard a random card. If you do, flip your Super Hero face up."}
   {:name "Darkseid (Imp.)"
    :cost "10+"
    :victory 5
    :text "You may gain all Super Powers in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Darkseid costs 3 more to defeat for each Super Power in the Line-Up."
    :first-appearance-attack "Each player puts all Super Powers from his hand into the Line-Up."}
   {:name "Deathstroke (Imp.)"
    :cost 10
    :victory 5
    :text "You may gain a Hero and Villain from the Line-Up. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing nil
    :first-appearance-attack "Each player destroys a Hero, Super Power, and Equipment in his hand."}
   {:name "Hades (Imp.)"
    :cost 12
    :victory 6
    :text "Gain a card from the destroyed pile and put it into your hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player puts the top two cards of the main deck into the Line-Up. Gain Weaknesses equal to the cost of one of those cards."}
   {:name "The Joker (Imp.)"
    :cost 10
    :victory 5
    :text "Each foe chooses: They discard two random cards or you draw two cards."
    :power 2
    :stack-ongoing nil
    :first-appearance-attack "Discard a random card. If its cost is 0, gain a Weakness and repeat this."}
   {:name "Lex Luthor (Imp.)"
    :cost 11
    :victory 5
    :text "Draw four cards."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "This Attack can't be avoided. Each player gains X Weaknesses, where X is equal to the highest VP value among cards in his hand. (* = 3.)"}
   {:name "Parallax (Imp.)"
    :cost 13
    :victory 6
    :text "Draw a card. Double your Power this turn."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player reveals his hand and discards all cards with cost 3 or less."}
   {:name "Sinestro (Imp.)"
    :cost "10+"
    :victory 5
    :text "You may gain all Heroes in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Sinestro costs 3 more to defeat for each Hero in the Line-Up."
    :first-appearance-attack "Each player puts all Heroes from his hand into the Line-Up."}])

(def all
  (->> crisis1
       (map #(assoc % :type :super-villain))))
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
(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :villain     "VI"})

(def card-spaces
  (array-map
   :super-villain {:facing :down
                   :type :stack}
   :timer         {:facing :down
                   :type :stack}
   :weakness      {:facing :down
                   :type :stack}
   :kick          {:facing :up
                   :type :stack}
   :destroyed     {:facing :down
                   :type :pile}
   :main-deck     {:facing :down
                   :type :stack}
   :line-up       {:facing :up
                   :type :pile}
   :super-hero {:facing :up
                :type :pile}
   :location   {:facing :up
                :type :pile}
   :hand       {:facing :up
                :type :pile}
   :deck       {:facing :down
                :type :stack}
   :discard    {:facing :down
                :type :pile}))

(def defaults
  {:punch-count 7
   :super-villain-count 6
   :vulnerability-count 3})
(ns dcdbg.print
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]))

;; helpers

(defn- mk-header [x]
  (-> x name str/upper-case))

;; printers

(defn print-card-summary! [card card-idx]
  (printf "%3s. " card-idx)
  (case (:type card)
    :super-villain
    (printf "%-3s VI %-1s %-1s %-1s %-2s %s\n"
            (or (:cost card) "")
            (if (:text card) "T" "")
            (if (:first-appearance-attack card) "A" "")
            (if (:stack-ongoing card) "G" "")
            (or (:power card) "")
            (:name card))

    :super-hero
    (println (:name card))

    (printf "%-3s %-2s %-1s %-1s%-1s%-1s %-2s %s\n"
            (or (:cost card) "")
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (if (:text card) "T" "")
            (if (:attack card) "A" "")
            (if (:defense card) "D" "")
            (if (:ongoing card) "G" "")
            (or (:power card) "")
            (:name card))))

(defn print-card-details! [card]
  (doseq [k [:name :type :cost :victory :text :power :attack :defense :ongoing :stack-ongoing :first-appearance-attack]]
    (when-let [x (k card)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile! [cards space-idx space-name]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header space-name) (count cards))
  (->> cards
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary! c idx))))
       (dorun)))

(defn print-stack! [cards space-idx space-name]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header space-name) (count cards))
  (when (-> cards first :facing (= :up))
    (print-card-summary! (first cards) 0)))

(defn print-game! [{:keys [messages state]}]
  (->> state
       (map-indexed (fn [idx {:keys [name cards]}]
                      (case (-> cfg/card-spaces name :type)
                        :pile (print-pile! cards idx name)
                        :stack (print-stack! cards idx name))))
       (dorun))
  (when-let [msgs (seq messages)]
    (println)
    (doseq [msg msgs]
      (println msg))))
(ns dcdbg.setup
  (:require [dcdbg.cards.core :as cards]
            [dcdbg.cards.equipment :as equipment]
            [dcdbg.cards.hero :as hero]
            [dcdbg.cards.location :as location]
            [dcdbg.cards.super-hero :as super-hero]
            [dcdbg.cards.super-power :as super-power]
            [dcdbg.cards.super-villain :as super-villain]
            [dcdbg.cards.villain :as villain]
            [dcdbg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- mk-cards [card-spec]
  (let [n (:copies card-spec 1)]
    (->> (dissoc card-spec :copies)
         (repeat n))))

(defn- flip [cards facing]
  (map #(assoc % :facing facing) cards))

(defn- use-facing [cards k]
  (let [facing (-> cfg/card-spaces k :facing)]
    (flip cards facing)))

;; setup

(defn- setup-deck []
  (->> [[cards/punch (-> cfg/defaults :punch-count)]
        [cards/vulnerability (-> cfg/defaults :vulnerability-count)]]
       (mapcat (fn [[card-spec n]]
                 (->> card-spec
                      (mk-cards)
                      (take n))))
       (rand/shuffle*)))

(defn- setup-main-deck []
  (->> (concat equipment/all
               hero/all
               location/all
               super-power/all
               villain/all)
       (mapcat mk-cards)
       (rand/shuffle*)))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] super-hero/all]
    (-> [x (rand/rand-nth* xs)]
        (use-facing :super-hero))))

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x y & z] super-villain/all
        z (->> z
               (rand/shuffle*)
               (take (- n 2)))]
    ;; flip Ra's Al-Ghul up, set Crisis Anti-Monitor on bottom
    (concat (-> [x] (flip :up))
            (->  z  (use-facing :super-villain))
            (-> [y] (use-facing :super-villain)))))

(defn mk-game-state [game]
  (let [svs (setup-super-villains (:super-villain-count cfg/defaults))
        [line-up main-deck] (split-at 5 (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at 5 (setup-deck))
        msgs (concat (->> shs
                          (map #(format "SUPER-HERO: %s" (:text %))))
                     [(->> svs
                           (first)
                           (:stack-ongoing)
                           (format "SUPER-VILLAIN ONGOING: %s"))])
        state [{:name :super-villain
                :cards svs}
               {:name :timer
                :cards (-> cards/weakness mk-cards (use-facing :weakness))}
               {:name :weakness
                :cards []}
               {:name :kick
                :cards (-> cards/kick mk-cards (use-facing :kick))}
               {:name :destroyed
                :cards []}
               {:name :main-deck
                :cards (-> main-deck (use-facing :main-deck))}
               {:name :line-up
                :cards (-> line-up (use-facing :line-up))}
               {:name :super-hero
                :cards shs}
               {:name :location
                :cards []}
               {:name :hand
                :cards (-> hand (use-facing :hand))}
               {:name :deck
                :cards (-> deck (use-facing :deck))}
               {:name :discard
                :cards []}]]
    (-> game
        (assoc :messages msgs)
        (assoc :state state))))
(ns dcdbg.commands
  (:require [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

;; helpers

(defn- find-space-index [game space-name]
  (->> game
       (:state)
       (keep-indexed (fn [idx space]
                       (when (-> space :name (= space-name))
                         idx)))
       (first)))

(defn- get-cards [game space-idx]
  (-> game :state (nth space-idx) :cards))

(defn- get-card [game space-idx card-idx]
  (-> game (get-cards space-idx) (nth card-idx)))

(defn- get-space-name [game space-idx]
  (-> game :state (nth space-idx) :name))

(defn- count-cards [game space-idx]
  (-> game (get-cards space-idx) count))

(defn- add-cards [cards new-cards top-or-bottom]
  (case top-or-bottom
    :top (concat new-cards cards)
    :bottom (concat cards new-cards)))

(defn- remove-cards [cards card-idxs]
  (let [card-idx-set (set card-idxs)]
    (keep-indexed (fn [idx card]
                    (when-not (card-idx-set idx)
                      card))
                  cards)))

(defn- update-cards [game space-idx update-fn]
  (update-in game [:state space-idx :cards] update-fn))

(defn- move* [game from-space-idx to-space-idx to-top-or-bottom card-idxs]
  (let [from-cards (get-cards game from-space-idx)
        to-space-name (get-space-name game to-space-idx)
        to-facing (-> cfg/card-spaces to-space-name :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-space-idx #(remove-cards % card-idxs))
        (update-cards to-space-idx #(add-cards % moved to-top-or-bottom)))))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game space-idx]
   (-> game
       (get-cards space-idx)
       ;; turn everything face-up so they are all visible
       (->> (map #(assoc % :facing :up)))
       (print/print-pile! space-idx (get-space-name game space-idx))))
  ([game k card-idx & card-idxs]
   (print/print-card-details! (get-card game k card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game k idx)))))

(defn move [game from-space-idx to-space-idx to-top-or-bottom & card-idxs]
  (move* game from-space-idx to-space-idx to-top-or-bottom card-idxs))

(defn gain
  ([game space-idx]
   (gain game space-idx 1))
  ([game space-idx n]
   (let [discard-idx (find-space-index game :discard)]
     (move* game space-idx discard-idx :top (range n)))))

(defn refill-deck [game]
  (let [deck-idx (find-space-index game :deck)
        discard-idx (find-space-index game :discard)
        discard-count (count-cards game discard-idx)
        shuffled (update-cards game discard-idx rand/shuffle*)]
    (move* shuffled discard-idx deck-idx :bottom (range discard-count))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-idx (find-space-index game :deck)
         deck-count (count-cards game deck-idx)
         discard-idx (find-space-index game :discard)
         discard-count (count-cards game discard-idx)
         hand-idx (find-space-index game :hand)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move* game deck-idx hand-idx :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game
           (refill-deck)
           (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [discard-idx (find-space-index game :discard)
        hand-idx (find-space-index game :hand)
        hand-count (count-cards game hand-idx)]
    (move* game hand-idx discard-idx :top (range hand-count))))

(defn refill-line-up [game]
  (let [main-deck-idx (find-space-index game :main-deck)
        line-up-idx (find-space-index game :line-up)
        card (get-card game main-deck-idx 0)
        msg (when-let [a (:attack card)]
              [(format "VILLAIN ATTACK: %s" a)])]
    (-> game
        (move main-deck-idx line-up-idx :top 0)
        (update :messages concat msg))))

(defn flip-super-villain [game]
  (let [super-villain-idx (find-space-index game :super-villain)
        [sv & svs] (get-cards game super-villain-idx)
        flipped (assoc sv :facing :up)
        msgs (concat (when-let [so (:stack-ongoing sv)]
                       [(format "SUPER-VILLAIN ONGOING: %s" so)])
                     (when-let [faa (:first-appearance-attack sv)]
                       [(format "SUPER-VILLAIN ATTACK: %s" faa)]))]
    (cond-> game
      ;; if top super-villain is face-down, flip it up and show effects
      (-> sv :facing (= :down))
      (-> (update-cards super-villain-idx (constantly (cons flipped svs)))
          (update :messages concat msgs)))))

(defn advance-timer [game]
  (let [timer-idx (find-space-index game :timer)
        weakness-idx (find-space-index game :weakness)]
    (move game timer-idx weakness-idx :top 0)))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (refill-line-up)
       (flip-super-villain)
       (advance-timer))))
(ns dcdbg.core
  (:require [dcdbg.commands :as cmds]
            [dcdbg.print :as print]
            [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def game-atom (atom nil))

(def help-atom (atom []))

(def meta-fn-map
  {:mk-game-state setup/mk-game-state
   :print-game print/print-game!})

(def command-map
  (array-map
   :pg {:doc "(print game): [space-idx [card-idx+]]"
        :fn cmds/print!}
   :pt {:doc "(print top): space-idx"
        :fn #(cmds/print! %1 %2 0)}
   :mt {:doc "(move to top): from-space-idx to-space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-space-idx to-space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :pl {:doc "(play location): card-idx+"
        :fn #(apply cmds/move %1 9 8 :bottom %&)}
   :bl {:doc "(buy line-up): card-idx+"
        :fn #(apply cmds/move %1 6 11 :top %&)}
   :ga {:doc "(gain): space-idx [n]"
        :fn cmds/gain}
   :di {:doc "(discard): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 11 :top %&)}
   :de {:doc "(destroy): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 4 :top %&)}
   :rd {:doc "(refill deck)"
        :fn cmds/refill-deck}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :rl {:doc "(refill line-up)"
        :fn cmds/refill-line-up}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
