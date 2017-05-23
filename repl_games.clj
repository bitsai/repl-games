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
(ns btdg.cards)

(def characters
  [{:name "Bart Cassidy"
    :max-life 8
    :ability "You may take an arrow instead of losing a life point (except to Indians or Dynamite). You cannot use this ability if you lose a life point to Indian or Dynamite, only for 1, 2, or Gatling Gun. You may not use this ability to take the last arrow remaining in the pile."}
   {:name "Black Jack"
    :max-life 8
    :ability "You may re-roll Dynamite (not if you roll three or more!). If you roll three or more Dynamite at once (or in total if you didn't re-roll them), follow the usual rules (your turn ends, etc.)."}
   {:name "Calamity Jane"
    :max-life 8
    :ability "You can use 1 as 2 and vice-versa."}
   {:name "El Gringo"
    :max-life 7
    :ability "When a player makes you lose one or more life points, he must take an arrow. Life points lost to Indians or Dynamite are not affected."}
   {:name "Jesse Jones"
    :max-life 9
    :ability "If you have four life points or less, you gain two if you use Beer for yourself. For example, if you have four life points and use two beers, you get four life points."}
   {:name "Jourdonnais"
    :max-life 7
    :ability "You never lose more than one life point to Indians."}
   {:name "Kit Carlson"
    :max-life 7
    :ability "For each Gatling Gun you may discard one arrow from any player. You may choose to discard your own arrows. If you roll three Gatling Gun, you discard all your own arrows, plus three from any player(s) (of course, you still deal one damage to each other player)."}
   {:name "Lucky Duke"
    :max-life 8
    :ability "You may make one extra re-roll. You may roll the dice a total of four times on your turn."}
   {:name "Paul Regret"
    :max-life 9
    :ability "You never lose life points to the Gatling Gun."}
   {:name "Pedro Ramirez"
    :max-life 8
    :ability "Each time you lose a life point, you may discard one of your arrows. You still lose the life point when you use this ability."}
   {:name "Rose Doolan"
    :max-life 9
    :ability "You may use 1 or 2 for players sitting one place further. With 1 you may hit a player sitting up to two places away, and with 2 you may hit a player sitting two or three places away."}
   {:name "Sid Ketchum"
    :max-life 8
    :ability "At the beginning of your turn, any player of your choice gains one life point. You may also choose yourself."}
   {:name "Slab the Killer"
    :max-life 8
    :ability "Once per turn, you can use a Beer to double a 1 or 2. The dice you double takes two life points away from the same player (you can't choose two different players). The Beer in this case does not provide any life points."}
   {:name "Suzy Lafayette"
    :max-life 8
    :ability "If you didn't roll any 1 or 2, you gain two life points. This only applies at the end of your turn, not during your re-rolls."}
   {:name "Vulture Sam"
    :max-life 9
    :ability "Each time another player is eliminated, you gain two life points."}
   {:name "Willy the Kid"
    :max-life 8
    :ability "You only need 2 Gatling Gun results to use the Gatling Gun. You can activate the Gatling Gun only once per turn, even if you roll more than two Gatling Gun results."}])
(ns btdg.config)

(def defaults
  {:sheriff-count 1
   :renegade-count 1
   :outlaw-count 3
   :deputy-count 1
   :arrow-count 9
   :dice-count 5})
(ns btdg.print
  (:require [clojure.string :as str]))

(defn- ->active-marker [active-idxs idx]
  (if (or (and (integer? active-idxs)
               (= active-idxs idx))
          (and (set? active-idxs)
               (contains? active-idxs idx)))
    ">"
    " "))

(defn- print-player! [active-player-idx player-idx player]
  (if-not (-> player :life pos?)
    (println (format "%s[%s] DEAD"
                     (->active-marker active-player-idx player-idx)
                     player-idx))
    (let [role (:role player)]
      (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                       (->active-marker active-player-idx player-idx)
                       player-idx
                       (-> role name str/upper-case)
                       (-> player :life)
                       (-> player :max-life)
                       (-> player :arrows)))
      (when (#{:sheriff :deputy} role)
        (println (format "     %s: %s"
                         (-> player :name)
                         (-> player :ability)))))))

(defn- print-die! [active-die-idxs die-idx die]
  (println (format "%s[%s] %s"
                   (->active-marker active-die-idxs die-idx)
                   die-idx
                   die)))

(defn print-game! [game]
  (let [active-player-idx (-> game :state :active-player-idx)
        active-die-idxs (-> game :state :active-die-idxs)]
    (->> game
         (:state)
         (:players)
         (map-indexed (partial print-player! active-player-idx))
         (dorun))
    (println (format " ARROWS %d" (-> game :state :arrows)))
    (println (format " DICE ROLLS %d" (-> game :state :dice-rolls)))
    (->> game
         (:state)
         (:dice)
         (map-indexed (partial print-die! active-die-idxs))
         (dorun))))
(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-in-game [game ks]
  (get-in game (cons :state ks)))

(defn- assoc-in-game [game ks v]
  (assoc-in game (cons :state ks) v))

(defn- update-in-game [game ks f & args]
  (apply update-in game (cons :state ks) f args))

(defn- get-in-player [game player-idx ks]
  (get-in-game game (concat [:players player-idx] ks)))

(defn- assoc-in-player [game player-idx ks v]
  (assoc-in-game game (concat [:players player-idx] ks) v))

(defn- update-in-player [game player-idx ks f & args]
  (apply update-in-game game (concat [:players player-idx] ks) f args))

(defn- do-for-players [f game player-idx n args]
  (reduce (fn [g [player-idx n]]
             (f g player-idx n))
           game
           (cons [player-idx n] (partition 2 args))))

(defn- roll-die []
  (case (rand/uniform 0 6)
    0 "ARROW"
    1 "DYNAMITE"
    2 "1"
    3 "2"
    4 "BEER"
    5 "GATLING GUN"))

(defn- next-active-player-idx [game]
  (let [n (-> game (get-in-game [:players]) count)
        active-player-idx (get-in-game game [:active-player-idx])]
    (->> (range 1 (inc n))
         ;; generate seq of next player idxs
         (map #(-> active-player-idx (+ %) (mod n)))
         ;; find live player idxs
         (filter #(-> game (get-in-player % [:life]) pos?))
         ;; get the next one
         (first))))

;; commands

(defn init-dice [game]
  (let [n (:dice-count cfg/defaults)]
    (-> game
        ;; set # dice rolls to 1
        (assoc-in-game [:dice-rolls] 1)
        ;; roll all dice
        (assoc-in-game [:dice] (vec (repeatedly n roll-die)))
        ;; mark all dice as active
        (assoc-in-game [:active-die-idxs] (set (range n))))))

(defn reroll-dice
  ([game]
   (let [n (:dice-count cfg/defaults)]
     ;; by default, reroll all dice
     (apply reroll-dice game (range n))))
  ([game & die-idxs]
   (reduce (fn [g die-idx]
             ;; roll selected die
             (assoc-in-game g [:dice die-idx] (roll-die)))
           (-> game
               ;; increment # dice rolls
               (update-in-game [:dice-rolls] inc)
               ;; mark selected dice as active
               (assoc-in-game [:active-die-idxs] (set die-idxs)))
           die-idxs)))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [game-arrows (get-in-game game [:arrows])
         arrows (-> n
                    ;; can't take more than game arrows
                    (min game-arrows))]
     (-> game
         (update-in-game [:arrows] - arrows)
         (update-in-player player-idx [:arrows] + arrows))))
  ([game player-idx n & args]
   (do-for-players take-arrows game player-idx n args)))

(defn discard-arrows
  ([game player-idx]
   (let [player-arrows (get-in-player game player-idx [:arrows])]
     ;; by default, discard all player arrows
     (discard-arrows game player-idx player-arrows)))
  ([game player-idx n]
   (let [player-arrows (get-in-player game player-idx [:arrows])
         arrows (-> n
                    ;; can't discard more than player arrows
                    (min player-arrows))]
     (-> game
         (update-in-player player-idx [:arrows] - arrows)
         (update-in-game [:arrows] + arrows))))
  ([game player-idx n & args]
   (do-for-players discard-arrows game player-idx n args)))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-in-player game player-idx [:max-life])
         add-life #(-> %
                       (+ n)
                       ;; can't go above max life
                       (min max-life))]
     (update-in-player game player-idx [:life] add-life)))
  ([game player-idx n & args]
   (do-for-players gain-life game player-idx n args)))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [remove-life #(-> %
                          (- n)
                          ;; can't go below 0 life
                          (max 0))
         updated (update-in-player game player-idx [:life] remove-life)]
     (cond-> updated
       ;; if player is dead, discard arrows
       (-> updated (get-in-player player-idx [:life]) zero?)
       (discard-arrows player-idx))))
  ([game player-idx n & args]
   (do-for-players lose-life game player-idx n args)))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack all players
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (let [idxs-and-arrows (->> player-idxs
                              (map #(get-in-player game % [:arrows]))
                              (interleave player-idxs))]
     (-> game
         (#(apply lose-life % idxs-and-arrows))
         (#(apply discard-arrows % idxs-and-arrows))))))

(defn gatling-gun
  ([game]
   (let [active-player-idx (get-in-game game [:active-player-idx])
         player-idxs (-> game (get-in-game [:players]) count range)]
     ;; by default, attack each of the OTHER players
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [active-player-idx (get-in-game game [:active-player-idx])
         idxs-and-damages (interleave player-idxs (repeat 1))]
     (-> game
         (#(apply lose-life % idxs-and-damages))
         (discard-arrows active-player-idx)))))

(defn end-turn [game]
  (-> game
      ;; find next active player
      (assoc-in-game [:active-player-idx] (next-active-player-idx game))
      ;; init dice again
      (init-dice)))
(ns btdg.setup
  (:require [btdg.cards :as cards]
            [btdg.commands :as cmds]
            [btdg.config :as cfg]
            [repl-games.random :as rand]))

(defn- setup-roles [sheriff-count renegade-count outlaw-count deputy-count]
  ;; put sheriffs first, then the other roles shuffled together
  (concat (repeat sheriff-count :sheriff)
          (-> (concat (repeat renegade-count :renegade)
                      (repeat outlaw-count :outlaw)
                      (repeat deputy-count :deputy))
              (rand/shuffle*))))

(defn- setup-characters [n]
  (->> cards/characters
       (rand/shuffle*)
       (take n)))

(defn- setup-players [roles characters]
  (mapv (fn [idx r c]
          (let [max-life (cond-> (:max-life c)
                           ;; sheriff gets 2 additional life points
                           (= r :sheriff)
                           (+ 2))]
            (-> c
                (assoc :role r)
                (assoc :max-life max-life)
                (assoc :life max-life)
                (assoc :arrows 0))))
        (range)
        roles
        characters))

(defn mk-game-state [game]
  (let [roles (setup-roles (:sheriff-count cfg/defaults)
                           (:renegade-count cfg/defaults)
                           (:outlaw-count cfg/defaults)
                           (:deputy-count cfg/defaults))
        characters (setup-characters (count roles))
        players (setup-players roles characters)
        state {:players players
               ;; make first player active
               :active-player-idx 0
               :arrows (:arrow-count cfg/defaults)}]
    (-> game
        ;; set initial state
        (assoc :state state)
        ;; init dice
        (cmds/init-dice))))
(ns btdg.core
  (:require [btdg.commands :as cmds]
            [btdg.print :as print]
            [btdg.setup :as setup]
            [repl-games.core :as repl-games]))

(def game-atom (atom nil))

(def help-atom (atom []))

(def meta-fn-map
  {:mk-game-state setup/mk-game-state
   :print-game print/print-game!})

(def command-map
  (array-map
   :pg {:doc "(print game)"
        :fn print/print-game!}
   :rd {:doc "(reroll dice): [die-idx ...]"
        :fn cmds/reroll-dice}
   :ta {:doc "(take arrows): player-idx [n] ..."
        :fn cmds/take-arrows}
   :da {:doc "(discard arrows): player-idx [n] ..."
        :fn cmds/discard-arrows}
   :gl {:doc "(gain life): player-idx [n] ..."
        :fn cmds/gain-life}
   :ll {:doc "(lose life): player-idx [n] ..."
        :fn cmds/lose-life}
   :ia {:doc "(Indians attack): [player-idx ...]"
        :fn cmds/indians-attack}
   :gg {:doc "(Gatling Gun): [player-idx ...]"
        :fn cmds/gatling-gun}
   :et {:doc "(end turn)"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
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
    :power 2
    :text "You may put any one card you buy or gain this turn on top of your deck."
    :copies 3}
   {:name "Batmobile"
    :cost 2
    :victory 1
    :power "1?"
    :text "If this is the first card you play this turn, discard your hand and draw five cards. Otherwise, +1 Power."
    :copies 3}
   {:name "The Bat-Signal"
    :cost 5
    :victory 1
    :power 1
    :text "Put a Hero from your discard pile into your hand."
    :copies 3}
   {:name "The Cape and Cowl"
    :cost 4
    :victory 1
    :power 2
    :defense "You may discard this card to avoid an Attack. If you do, draw two cards."
    :copies 3}
   {:name "Green Arrow's Bow"
    :cost 4
    :victory 1
    :power 2
    :text "Super-Villains cost you 2 less to defeat this turn."
    :copies 3}
   {:name "Lasso of Truth"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card."
    :power 1
    :copies 3}
   {:name "Nth Metal"
    :cost 3
    :victory 1
    :power 1
    :text "Look at the top card of your deck. You may destroy it."
    :copies 4}
   {:name "Power Ring"
    :cost 3
    :victory 1
    :power "2+"
    :text "Reveal the top card of your deck. If its cost is 1 or greater, +3 Power. Otherwise, +2 Power."
    :copies 3}
   {:name "Utility Belt"
    :cost 5
    :victory "5?"
    :power 2
    :text "At the end of the game, if you have four or more other Equipment in your deck, this card is worth 5 VPs."
    :copies 3}])

(def forever-evil
  [{:name "Broadsword"
    :cost 6
    :victory 2
    :attack "Destroy a card with cost 1, 2, or 3 in a foe's discard pile."
    :power 2
    :copies 2}
   {:name "Cold Gun"
    :cost 2
    :victory 1
    :power 1
    :text "You may put a Frozen token on a card in the Line-Up. If you do, remove it at the start of your next turn."
    :copies 3}
   {:name "Cosmic Staff"
    :cost 5
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, gain the bottom card of the main deck."
    :power 2
    :copies 3}
   {:name "Firestorm Matrix"
    :cost 7
    :victory 2
    :text "Play the top card of your deck. If its cost is 5 or less, you may destroy this card. If you do, leave the card you played in front of you for the rest of the game and it has \"Ongoing: You may play this card once during each of your turns. (At end of game, destroy it.)\""
    :copies 2}
   {:name "Mallet"
    :cost 4
    :victory 1
    :text "Reveal the top card of your deck. Draw it or pass it to any player's discard pile."
    :copies 3}
   {:name "Man-Bat Serum"
    :cost 3
    :victory 1
    :power "0+"
    :text "+Power equal to your VPs. If you have 5 or more VPs, destroy this card at the end of your turn."
    :copies 3}
   {:name "Pandora's Box"
    :cost 2
    :victory 1
    :text "Reveal the top card of the main deck. Add cards from the main deck to the Line-Up equal to the revealed card's cost."
    :copies 2}
   {:name "Power Armor"
    :cost 8
    :victory 3
    :defense "You may reveal this card from your hand to avoid an Attack. If you do, you may destroy a card in your hand or discard pile."
    :power 3
    :copies 1}
   {:name "Secret Society Communicator"
    :cost 4
    :victory 1
    :power "2+"
    :text "You may destroy a card in your hand or discard pile. If it's a Hero, +Power equal to its Cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Sledgehammer"
    :cost 3
    :victory 1
    :text "You may destroy a non-Equipment card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "Venom Injector"
    :cost 1
    :victory 0
    :power "2?"
    :text "When you destroy this card in any zone, +2 Power."
    :copies 2}])

(def crisis1
  [{:name "Bo Staff"
    :cost 3
    :victory 1
    :power "2+"
    :text "+3 Power if there are one or more Villains in the Line-Up. Otherwise, +2 Power."
    :copies 2}
   {:name "Magician's Corset"
    :cost 5
    :victory 1
    :power "0+"
    :text "Choose any number of players. Each of those players draws a card. +1 Power for each player who draws a card this way (including you)."
    :copies 1}
   {:name "Quiver of Arrows"
    :cost 1
    :victory 1
    :text "Choose any number of players. Each of those players reveals the top card of his deck and may discard it."
    :copies 2}
   {:name "Signature Trenchcoat"
    :cost 4
    :victory 1
    :power "2?"
    :text "You may gain a card with cost 5 or less from the destroyed pile. If you choose not to, +2 Power."
    :copies 2}])

(def all
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :equipment))))
(ns dcdbg.cards.hero)

(def base
  [{:name "Blue Beetle"
    :cost 6
    :victory 2
    :defense "You may reveal this card from your hand to avoid an Attack. (It stays in your hand.)"
    :power 3
    :copies 1}
   {:name "Catwoman"
    :cost 2
    :victory 1
    :power 2
    :copies 3}
   {:name "Dark Knight"
    :cost 5
    :victory 1
    :power 2
    :text "Gain all Equipment in the Line-Up. Then, if you play or have played Catwoman this turn, you may put a card you bought or gained this turn into your hand."
    :copies 1}
   {:name "Emerald Knight"
    :cost 5
    :victory 1
    :text "Remove an Equipment, Hero, or Super Power from the Line-Up. Play it, then return it to the Line-Up at the end of your turn."
    :copies 1}
   {:name "The Fastest Man Alive"
    :cost 5
    :victory 1
    :text "Draw two cards."
    :copies 1}
   {:name "Green Arrow"
    :cost 5
    :victory "5?"
    :power 2
    :text "At the end of the game, if you have four or more other Heroes in your deck, this card is worth 5 VPs."
    :copies 3}
   {:name "High-Tech Hero"
    :cost 3
    :victory 1
    :power "1+"
    :text "If you play or have played a Super Power or Equipment this turn, +3 Power. Otherwise, +1 Power."
    :copies 3}
   {:name "J'onn J'onnz"
    :cost 6
    :victory 2
    :text "Play the top card of the Super-Villain stack, then return it to the top of the stack. (The First Appearance -- Attack does not happen.)"
    :copies 1}
   {:name "Kid Flash"
    :cost 2
    :victory 1
    :text "Draw a card."
    :copies 4}
   {:name "King of Atlantis"
    :cost 5
    :victory 1
    :power "1+"
    :text "You may destroy a card in your discard pile. If you do, +3 Power. Otherwise, +1 Power."
    :copies 1}
   {:name "Man of Steel"
    :cost 8
    :victory 3
    :power 3
    :text "Put all Super Powers from your discard pile into your hand."
    :copies 1}
   {:name "Mera"
    :cost 3
    :victory 1
    :power "2+"
    :text "If your discard pile is empty, +4 Power. Otherwise, +2 Power."
    :copies 2}
   {:name "Princess Diana of Themyscira"
    :cost 7
    :victory 2
    :text "Gain all Villains with cost 7 or less in the Line-Up."
    :copies 1}
   {:name "Robin"
    :cost 3
    :victory 1
    :power 1
    :text "Put an Equipment from your discard pile into your hand."
    :copies 3}
   {:name "Supergirl"
    :cost 4
    :victory 1
    :text "You may put a Kick from the Kick stack into your hand."
    :copies 2}
   {:name "Swamp Thing"
    :cost 4
    :victory 1
    :power "2+"
    :text "If you control a Location, +5 Power. Otherwise, +2 Power."
    :copies 2}
   {:name "Zatanna Zatara"
    :cost 4
    :victory 1
    :power 1
    :text "You may put up to two cards from your discard pile on the bottom of your deck."
    :copies 2}])

(def forever-evil
  [{:name "Amanda Waller"
    :cost 4
    :victory 1
    :power "2?"
    :text "You may destroy a card in your hand or discard pile. If it's a Villain, +Power equal to its cost. If you choose not to, +2 Power."
    :copies 2}
   {:name "Catwoman"
    :cost 3
    :victory 1
    :attack "Steal 1 VP from a foe."
    :power 1
    :copies 2}
   {:name "Commissioner Gordon"
    :cost 2
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, gain 1 VP."
    :power 1
    :copies 3}
   {:name "Dr. Light"
    :cost 3
    :victory 1
    :attack "A foe puts a Location he controls into his discard pile."
    :text "Draw a card."
    :copies 2}
   {:name "Element Woman"
    :cost 4
    :victory 1
    :power 2
    :text "While you own or are playing this card, it is also a Super Power, Equipment, and Villain."
    :copies 2}
   {:name "Firestorm"
    :cost 6
    :victory 2
    :text "Put the top card of your deck on your Super-Villain. This card has the game text of each card on your Super-Villain this turn. (At end of game, destroy those cards.)"
    :copies 1}
   {:name "Pandora"
    :cost 7
    :victory 2
    :power "1+"
    :text "Add the top card of the Main Deck to the Line-Up. +1 Power for each different cost among cards in the Line-Up."
    :copies 1}
   {:name "Phantom Stranger"
    :cost 5
    :victory "10-"
    :text "You may destroy a card in your hand and you may destroy a card in your discard pile. At end of game, this card is worth 1 fewer VP for each card with cost 0 in your deck. (Minimum 0)"
    :copies 1}
   {:name "Power Girl"
    :cost 5
    :victory 2
    :power 3
    :copies 3}
   {:name "Stargirl"
    :cost 4
    :victory 1
    :defense "You may discard this card to avoid an Attack. If you do, draw a card and put a card with cost 1 or greater from the destroyed pile on the bottom of the main deck."
    :power 2
    :copies 2}
   {:name "Steel"
    :cost 3
    :victory 1
    :text "You may destroy a non-Hero card in your hand or discard pile. If its cost is 1 or greater, gain 1 VP."
    :copies 2}
   {:name "Steve Trevor"
    :cost 1
    :victory 0
    :text "When you destroy this card in any zone, draw two cards, and then discard a card."
    :copies 2}
   {:name "Vibe"
    :cost 2
    :victory 1
    :power 1
    :text "You may put a Hero or Super Power with cost 5 or less from your discard pile on top of your deck."
    :copies 2}])

(def crisis1
  [{:name "Animal Man"
    :cost 4
    :victory 1
    :power 2
    :text "Choose a card type. Reveal the top card of your deck. If it has that card type, draw it."
    :copies 3}
   {:name "Captain Atom"
    :cost 6
    :victory 2
    :power "2?"
    :text "You may gain a card with cost 4 or less from the Line-Up, and then put it into any player's hand. If you choose not to, +2 Power."
    :copies 2}
   {:name "John Constantine"
    :cost 5
    :victory 1
    :text "Reveal the top two cards of your deck and draw one of them. Put the other on top of your deck or destroy it."
    :copies 2}])

(def all
  (->> (concat base forever-evil crisis1)
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

(def forever-evil
  [{:name "Belle Reve"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Villain you play"
    :copies 1}
   {:name "Blackgate Prison"
    :cost 4
    :victory 1
    :ongoing "Once during each of your turns, reveal the top card of your deck. If it's a Vulnerability or Weakness, destroy it and gain 1 VP."
    :copies 1}
   {:name "Central City"
    :cost 4
    :victory 1
    :ongoing "+1 Power for each non-Kick Super Power you play."
    :copies 1}
   {:name "Earth-3"
    :cost 6
    :victory 1
    :ongoing "Once during each of your turns, reveal the top card of your deck. If it's a Punch, destroy it and gain 1 VP."
    :copies 1}
   {:name "Happy Harbor"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Hero you play."
    :copies 1}
   {:name "S.T.A.R. Labs"
    :cost 5
    :victory 1
    :ongoing "+1 Power for each Equipment you play."
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

(def all
  (->> (concat base forever-evil crisis1)
       (map #(assoc % :type :location))))
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
    :ongoing "Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."}
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
(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :villain     "VI"})

(def defaults
  {:hand-size 5
   :line-up-size 5
   :punch-count 7
   :super-villain-count 8
   :vulnerability-count 3})

(def zones
  (array-map
   :super-villain {:type :stack
                   :facing :down}
   :countdown     {:type :stack
                   :facing :down}
   :weakness      {:type :stack
                   :facing :up}
   :kick          {:type :stack
                   :facing :up}
   :destroyed     {:type :pile
                   :facing :down}
   :main-deck     {:type :stack
                   :facing :down}
   :line-up       {:type :pile
                   :facing :up}
   :super-hero {:type :pile
                :facing :up}
   :location   {:type :pile
                :facing :up}
   :hand       {:type :pile
                :facing :up}
   :deck       {:type :stack
                :facing :down}
   :discard    {:type :pile
                :facing :down}))
(ns dcdbg.print
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]))

;; helpers

(defn- mk-header [x]
  (-> x name str/upper-case))

;; printers

(defn print-card-summary! [card card-idx]
  (printf "%3s. " card-idx)
  (if (-> card :type (= :super-hero))
    (println (:name card))
    (printf "%-3s %-2s %-1s%-1s %-2s %s\n"
            (or (:cost card) "")
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (if (:text card) "T" "")
            (cond
              (:defense card) "D"
              (:ongoing card) "O"
              :else "")
            (or (:power card) "")
            (:name card))))

(defn print-card-details! [card]
  (doseq [k [:name :type :cost :victory :defense :attack :ongoing :power :text]]
    (when-let [x (k card)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile! [{:keys [name cards]} zone-idx]
  (printf "[%2s] %s (%d)\n" zone-idx (mk-header name) (count cards))
  (->> cards
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary! c idx))))
       (dorun)))

(defn print-stack! [{:keys [name cards]} zone-idx]
  (printf "[%2s] %s (%d)\n" zone-idx (mk-header name) (count cards))
  (when (-> cards first :facing (= :up))
    (print-card-summary! (first cards) 0)))

(defn print-game! [{:keys [messages state]}]
  (->> state
       (map-indexed (fn [idx zone]
                      (case (:type zone)
                        :pile (print-pile! zone idx)
                        :stack (print-stack! zone idx))))
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
  (let [n (:copies card-spec)]
    (->> (dissoc card-spec :copies) (repeat n))))

(defn- flip [cards facing]
  (mapv #(assoc % :facing facing) cards))

;; setup

(defn- setup-deck []
  (->> [[cards/punch (-> cfg/defaults :punch-count)]
        [cards/vulnerability (-> cfg/defaults :vulnerability-count)]]
       (mapcat (fn [[card-spec n]]
                 (->> card-spec (mk-cards) (take n))))
       (rand/shuffle*)))

(defn- setup-main-deck []
  (->> (concat equipment/all
               hero/all
               location/all
               super-power/all
               villain/all)
       (mapcat mk-cards)
       ;; use only 1 copy of each card
       (distinct)
       (rand/shuffle*)))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] super-hero/all]
    [x (rand/rand-nth* xs)]))

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x y & zs] super-villain/all
        zs (->> zs (rand/shuffle*) (take (- n 2)))]
    ;; set Ra's Al-Ghul on top, Crisis Anti-Monitor on bottom
    (concat [x] zs [y])))

(defn mk-game-state [game]
  (let [{:keys [hand-size line-up-size super-villain-count]} cfg/defaults
        svs (setup-super-villains super-villain-count)
        [line-up main-deck] (split-at line-up-size (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at hand-size (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "SUPER HERO [%s] %s" (:name %) (:text %))))
                   (let [sv (first svs)]
                     (format "ONGOING [%s] %s" (:name sv) (:ongoing sv))))
        state (for [[name cards] [[:super-villain svs]
                                  [:countdown (mk-cards cards/weakness)]
                                  [:weakness []]
                                  [:kick (mk-cards cards/kick)]
                                  [:destroyed []]
                                  [:main-deck main-deck]
                                  [:line-up line-up]
                                  [:super-hero shs]
                                  [:location []]
                                  [:hand hand]
                                  [:deck deck]
                                  [:discard []]]
                    :let [{:keys [facing type]} (get cfg/zones name)]]
                {:name name
                 :type type
                 :facing facing
                 :cards (flip cards facing)})]
    (-> game
        (assoc :messages msgs)
        (assoc :state (vec state))
        ;; flip first super-villain face up
        (assoc-in [:state 0 :cards 0 :facing] :up))))
(ns dcdbg.commands
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

;; helpers

(defn- find-zone-index [game zone-name]
  (->> game
       (:state)
       (keep-indexed (fn [idx zone]
                       (when (-> zone :name (= zone-name))
                         idx)))
       (first)))

(defn- to-zone-index [game zone-*]
  (cond
    (keyword? zone-*) (find-zone-index game zone-*)
    (number? zone-*)  zone-*
    :else              (throw (Exception. "Invalid zone-*!"))))

(defn- get-zone [game zone-*]
  (let [zone-idx (to-zone-index game zone-*)]
    (-> game :state (nth zone-idx))))

(defn- get-cards [game zone-*]
  (-> game (get-zone zone-*) :cards))

(defn- get-card [game zone-* card-idx]
  (-> game (get-cards zone-*) (nth card-idx)))

(defn- count-cards [game zone-*]
  (-> game (get-cards zone-*) count))

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

(defn- update-cards [game zone-* update-fn]
  (let [zone-idx (to-zone-index game zone-*)]
    (update-in game [:state zone-idx :cards] update-fn)))

(defn- move* [game from-* to-* top-or-bottom & card-idxs]
  (let [card-idxs (or (seq card-idxs) [0])
        from-cards (get-cards game from-*)
        to-facing (-> game (get-zone to-*) :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-* #(remove-cards % card-idxs))
        (update-cards to-* #(add-cards % moved top-or-bottom)))))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game zone-*]
   (let [zone-idx (to-zone-index game zone-*)]
     (-> game
         ;; turn cards face-up so they are all visible
         (update-cards zone-* (fn [cards]
                                 (map #(assoc % :facing :up) cards)))
         (get-zone zone-*)
         (print/print-pile! zone-idx))))
  ([game zone-* card-idx & card-idxs]
   (print/print-card-details! (get-card game zone-* card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game zone-* idx)))))

(defn move [game from-* to-* top-or-bottom card-idxs]
  (apply move* game from-* to-* top-or-bottom card-idxs))

(defn gain
  ([game zone-*]
   (gain game zone-* 1))
  ([game zone-* n]
   (move game zone-* :discard :top (range n))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-count (count-cards game :deck)
         discard-count (count-cards game :discard)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move game :deck :hand :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game
           (update-cards :discard rand/shuffle*)
           (move :discard :deck :bottom (range discard-count))
           (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "Not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (move game :hand :discard :top (range hand-count))))

(defn exec-super-villain-plan [game]
  (let [line-up-count (count-cards game :line-up)]
    (move* game :line-up :destroyed :top (dec line-up-count))))

(defn refill-line-up [game]
  (if (-> game (count-cards :line-up) (>= (:line-up-size cfg/defaults)))
    game
    (let [{:keys [name type attack]} (get-card game :main-deck 0)
          msgs (when (and (#{:hero :villain} type) attack)
                 [(format "ATTACK [%s] %s" name attack)])]
      (-> game
          (update :messages concat msgs)
          (move* :main-deck :line-up :top 0)
          (refill-line-up)))))

(defn exec-villains-plan [game]
  (let [vs (->> (get-cards game :line-up)
                (filter #(-> % :type (= :villain))))]
    (if (empty? vs)
      game
      (let [max-cost (->> vs
                          (map :cost)
                          (apply max))]
        (move game :main-deck :destroyed :top (range max-cost))))))

(defn flip-super-villain [game]
  (if (-> game (get-card :super-villain 0) :facing (= :up))
    game
    (let [[sv & svs] (get-cards game :super-villain)
          {:keys [name attack ongoing]} sv
          msgs (concat (when attack
                         [(format "ATTACK [%s] %s" name attack)])
                       (when ongoing
                         [(format "ONGOING [%s] %s" name ongoing)]))
          new-svs (-> sv (assoc :facing :up) (cons svs))]
      (-> game
          (update :messages concat msgs)
          (update-cards :super-villain (constantly new-svs))))))

(defn advance-countdown [game]
  (move* game :countdown :weakness :top 0))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (exec-super-villain-plan)
       (refill-line-up)
       (exec-villains-plan)
       (flip-super-villain)
       (advance-countdown))))
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
   :pg {:doc "(print game): [zone-* [card-idx+]]"
        :fn cmds/print!}
   :mt {:doc "(move to top): from-* to-* [card-idx+]"
        :fn #(cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-* to-* [card-idx+]"
        :fn #(cmds/move %1 %2 %3 :bottom %&)}
   :ga {:doc "(gain): zone-* [n]"
        :fn cmds/gain}
   :bl {:doc "(buy line-up): [card-idx+]"
        :fn #(cmds/move %1 :line-up :discard :top %&)}
   :pl {:doc "(play location): [card-idx+]"
        :fn #(cmds/move %1 :hand :location :bottom %&)}
   :di {:doc "(discard): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :discard :top %&)}
   :de {:doc "(destroy): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :destroyed :top %&)}
   :dw {:doc "(destroy weakness): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :weakness :top %&)}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
