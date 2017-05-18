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
  [{:name "Ra's Al Ghul (Impossible)"
    :cost 9
    :victory 4
    :text "At the end of your turn, put this card onto the bottom of its owner's deck before drawing a new hand."
    :power 4
    :stack-ongoing "At the end of your turn, if you did not buy or gain a card from the Line-Up, add the top card of the main deck to the Line-Up."
    :first-appearance-attack nil}
   {:name "Crisis Anti-Monitor (Impossible)"
    :cost "13+"
    :victory nil
    :text nil
    :power nil
    :stack-ongoing "Add the top card of the main deck to the Line-Up. Whenever a Hero is added to the Line-Up, each player puts a Weakness from the stack on top of his deck. Crisis Anti-Monitor costs 1 more to defeat for each card in the Line-Up. Each player must defeat this Villain to win the game."
    :first-appearance-attack nil}
   {:name "Atrocitus (Impossible)"
    :cost 11
    :victory 5
    :text "Destroy up to three cards in your discard pile."
    :power 3
    :stack-ongoing nil
    :first-appearance-attack "Each player puts his hand under his Super Hero. If you avoid this Attack, put a random card from your hand under your Super Hero. When this Villain is defeated, discard those cards."}
   {:name "Black Manta (Impossible)"
    :cost "10+"
    :victory 5
    :text "You may gain all Equipment in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Black Manta costs 3 more to defeat for each Equipment in the Line-Up."
    :first-appearance-attack "Each player puts all Equipment from his hand into the Line-Up."}
   {:name "Brainiac (Impossible)"
    :cost 12
    :victory 6
    :text "Each foe reveals two random cards from his hand. Play one revealed non-Location card from each foe's hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player discards a random card, and then destroys the top X cards of the main deck, where X is the cost of that card."}
   {:name "Captain Cold (Impossible)"
    :cost 10
    :victory 5
    :text "An additional +1 Power for each foe with a Hero in his discard pile."
    :power "4+"
    :stack-ongoing nil
    :first-appearance-attack "Each player flips his Super Hero face down. When this Villain is defeated, each player may discard a random card. If you do, flip your Super Hero face up."}
   {:name "Darkseid (Impossible)"
    :cost "10+"
    :victory 5
    :text "You may gain all Super Powers in the Line-Up with cost 5 or less and put them into your hand. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing "Darkseid costs 3 more to defeat for each Super Power in the Line-Up."
    :first-appearance-attack "Each player puts all Super Powers from his hand into the Line-Up."}
   {:name "Deathstroke (Impossible)"
    :cost 10
    :victory 5
    :text "You may gain a Hero and Villain from the Line-Up. If you choose not to, +4 Power."
    :power "4?"
    :stack-ongoing nil
    :first-appearance-attack "Each player destroys a Hero, Super Power, and Equipment in his hand."}
   {:name "Hades (Impossible)"
    :cost 12
    :victory 6
    :text "Gain a card from the destroyed pile and put it into your hand."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player puts the top two cards of the main deck into the Line-Up. Gain Weaknesses equal to the cost of one of those cards."}
   {:name "The Joker (Impossible)"
    :cost 10
    :victory 5
    :text "Each foe chooses: They discard two random cards or you draw two cards."
    :power 2
    :stack-ongoing nil
    :first-appearance-attack "Discard a random card. If its cost is 0, gain a Weakness and repeat this."}
   {:name "Lex Luthor (Impossible)"
    :cost 11
    :victory 5
    :text "Draw four cards."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "This Attack can't be avoided. Each player gains X Weaknesses, where X is equal to the highest VP value among cards in his hand. (* = 3.)"}
   {:name "Parallax (Impossible)"
    :cost 13
    :victory 6
    :text "Draw a card. Double your Power this turn."
    :power nil
    :stack-ongoing nil
    :first-appearance-attack "Each player reveals his hand and discards all cards with cost 3 or less."}
   {:name "Sinestro (Impossible)"
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

(def defaults
  {:punch-count 7
   :super-villain-count 4
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
    (printf "%-3s SV %-1s %s\n"
            (or (:cost card) "")
            (if (:stack-ongoing card) "O" "")
            (:name card))

    :super-hero
    (println (:name card))

    (printf "%-3s %-2s %-1s%-1s%-1s %-2s %s\n"
            (or (:cost card) "")
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (if (:text card) "T" "")
            (if (:defense card) "D" "")
            (if (:ongoing card) "O" "")
            (or (:power card) "")
            (:name card))))

(defn print-card-details! [card]
  (doseq [k [:name :type :cost :victory :text :power :attack :defense :ongoing :stack-ongoing :first-appearance-attack]]
    (when-let [x (k card)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile! [{:keys [name cards]} space-idx]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header name) (count cards))
  (->> cards
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary! c idx))))
       (dorun)))

(defn print-stack! [{:keys [name cards]} space-idx]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header name) (count cards))
  (when (-> cards first :facing (= :up))
    (print-card-summary! (first cards) 0)))

(defn print-game! [{:keys [messages state]}]
  (->> state
       (map-indexed (fn [idx space]
                      (case (:type space)
                        :pile (print-pile! space idx)
                        :stack (print-stack! space idx))))
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
  (let [svs (setup-super-villains (:super-villain-count cfg/defaults))
        [line-up main-deck] (split-at 5 (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at 5 (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "SUPER-HERO: %s" (:text %))))
                   (->> svs
                        (first)
                        (:stack-ongoing)
                        (format "SUPER-VILLAIN ONGOING: %s")))
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
                    :let [{:keys [facing type]} (-> cfg/card-spaces name)]]
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

(defn- to-space-index [game space-*]
  (cond
    (keyword? space-*) (find-space-index game space-*)
    (number? space-*)  space-*
    :else              (throw (Exception. "Invalid space-*!"))))

(defn- get-space [game space-*]
  (let [space-idx (to-space-index game space-*)]
    (-> game :state (nth space-idx))))

(defn- get-cards [game space-*]
  (-> game (get-space space-*) :cards))

(defn- get-card [game space-* card-idx]
  (-> game (get-cards space-*) (nth card-idx)))

(defn- count-cards [game space-*]
  (-> game (get-cards space-*) count))

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

(defn- update-cards [game space-* update-fn]
  (let [space-idx (to-space-index game space-*)]
    (update-in game [:state space-idx :cards] update-fn)))

(defn- move* [game from-* to-* to-top-or-bottom card-idxs]
  (let [from-cards (get-cards game from-*)
        to-facing (-> game (get-space to-*) :facing)
        moved (->> card-idxs
                   (map #(nth from-cards %))
                   (map #(assoc % :facing to-facing)))]
    (-> game
        (update-cards from-* #(remove-cards % card-idxs))
        (update-cards to-* #(add-cards % moved to-top-or-bottom)))))

;; commands

(defn print!
  ([game]
   (print/print-game! game))
  ([game space-*]
   (let [space-idx (to-space-index game space-*)]
     (-> game
         ;; turn cards face-up so they are all visible
         (update-cards space-* (fn [cards]
                                 (map #(assoc % :facing :up) cards)))
         (get-space space-*)
         (print/print-pile! space-idx))))
  ([game space-* card-idx & card-idxs]
   (print/print-card-details! (get-card game space-* card-idx))
   (doseq [idx card-idxs]
     (println)
     (print/print-card-details! (get-card game space-* idx)))))

(defn move [game from-* to-* to-top-or-bottom & card-idxs]
  (move* game from-* to-* to-top-or-bottom card-idxs))

(defn defeat-super-villain [game]
  (update-in game [:state 0 :cards] rest))

(defn refill-line-up [game]
  (move game :main-deck :line-up :top 0))

(defn refill-deck [game]
  (let [discard-count (count-cards game :discard)
        shuffled (update-cards game :discard rand/shuffle*)]
    (move* shuffled :discard :deck :bottom (range discard-count))))

(defn draw
  ([game]
   (draw game 1))
  ([game n]
   (let [deck-count (count-cards game :deck)
         discard-count (count-cards game :discard)]
     (cond
       ;; if n <= deck size, draw
       (<= n deck-count)
       (move* game :deck :hand :bottom (range n))
       ;; if n > deck size and there are discards, refill deck then draw
       (pos? discard-count)
       (-> game (refill-deck) (draw n))
       ;; otherwise, throw exception
       :else
       (throw (Exception. "Not enough cards!"))))))

;; end turn helpers and command

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (move* game :hand :discard :top (range hand-count))))

(defn flip-super-villain [game]
  (let [[sv & svs] (get-cards game :super-villain)
        new-svs (-> (assoc sv :facing :up) (cons svs))
        msgs (concat (when-let [so (:stack-ongoing sv)]
                       [(format "SUPER-VILLAIN ONGOING: %s" so)])
                     (when-let [faa (:first-appearance-attack sv)]
                       [(format "SUPER-VILLAIN ATTACK: %s" faa)]))]
    (cond-> game
      ;; if top super-villain is face-down, flip it up and show effects
      (-> sv :facing (= :down))
      (-> (update-cards :super-villain (constantly new-svs))
          (update :messages concat msgs)))))

(defn advance-countdown [game]
  (move game :countdown :weakness :top 0))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (draw n)
       (refill-line-up)
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
   :pg {:doc "(print game): [space [card-idx+]]"
        :fn cmds/print!}
   :mt {:doc "(move to top): from to card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from to card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :dsv {:doc "(defeat super-villain)"
         :fn cmds/defeat-super-villain}
   :gw {:doc "(gain weakness)"
        :fn #(cmds/move %1 :weakness :discard :top 0)}
   :gk {:doc "(gain kick)"
        :fn #(cmds/move %1 :kick :discard :top 0)}
   :buy {:doc "(buy): card-idx+"
         :fn #(apply cmds/move %1 :line-up :discard :top %&)}
   :dv {:doc "(defeat villain): card-idx+"
        :fn #(apply cmds/move %1 :line-up :destroyed :top %&)}
   :rl {:doc "(refill line-up)"
        :fn cmds/refill-line-up}
   :di {:doc "(discard): card-idx+"
        :fn #(apply cmds/move %1 :hand :discard :top %&)}
   :de {:doc "(destroy): card-idx+"
        :fn #(apply cmds/move %1 :hand :destroyed :top %&)}
   :dw {:dos "(destroy weakness): card-idx+"
        :fn #(apply cmds/move %1 :hand :weakness :top %&)}
   :rd {:doc "(refill deck)"
        :fn cmds/refill-deck}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
