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
                  :messages nil}))

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
(ns btdg.characters)

(def base
  [{:name "BART CASSIDY"
    :max-life 8
    :ability "You may take an arrow instead of losing a life point (except to Indians or Dynamite)."}
   {:name "BLACK JACK"
    :max-life 8
    :ability "You may re-roll DYNAMITE (not if you roll three or more!)."}
   {:name "CALAMITY JANE"
    :max-life 8
    :ability "You can use 1 as 2 and vice-versa."}
   {:name "EL GRINGO"
    :max-life 7
    :ability "When a player makes you lose one or more life points, he must take an arrow."}
   {:name "JESSE JONES"
    :max-life 9
    :ability "If you have four life points or less, you gain two if you use BEER for yourself."}
   {:name "JOURDONNAIS"
    :max-life 7
    :ability "You never lose more than one life point to Indians."}
   {:name "KIT CARLSON"
    :max-life 7
    :ability "For each GATLING GUN you may discard one arrow from any player."}
   {:name "LUCKY DUKE"
    :max-life 8
    :ability "You may make one extra re-roll."}
   {:name "PAUL REGRET"
    :max-life 9
    :ability "You never lose life points to the Gatling Gun."}
   {:name "PEDRO RAMIREZ"
    :max-life 8
    :ability "Each time you lose a life point, you may discard one of your arrows."}
   {:name "ROSE DOOLAN"
    :max-life 9
    :ability "You may use 1 or 2 for players sitting one place further."}
   {:name "SID KETCHUM"
    :max-life 8
    :ability "At the beginning of your turn, any player of your choice gains one life point."}
   {:name "SLAB THE KILLER"
    :max-life 8
    :ability "Once per turn, you can use a BEER to double a 1 or 2."}
   {:name "SUZY LAFAYETTE"
    :max-life 8
    :ability "If you didn't roll any 1 or 2, you gain two life points."}
   {:name "VULTURE SAM"
    :max-life 9
    :ability "Each time another player is eliminated, you gain two life points."}
   {:name "WILLY THE KID"
    :max-life 8
    :ability "You only need two GATLING GUN to use the Gatling Gun."}])

(def old-saloon
  [{:name "JOSE DELGADO"
    :max-life 7
    :ability "You may use the Loudmouth die without replacing a base die (roll six dice total)."}
   {:name "TEQUILA JOE"
    :max-life 7
    :ability "You may use the Coward die without replacing a base die (roll six dice total)."}
   {:name "APACHE KID"
    :max-life 9
    :ability "If you roll ARROW, you may take the Indian Chief's Arrow from another player."}
   {:name "BILL NOFACE"
    :max-life 9
    :ability "Apply ARROW results only after your last roll."}
   {:name "ELENA FUENTE"
    :max-life 7
    :ability "Each time you roll one or more ARROW, you may give one of those arrows to a player of your choice."}
   {:name "VERA CUSTER"
    :max-life 7
    :ability "Each time you must lose life points for 1 or 2, you lose 1 life point less."}
   {:name "DOC HOLYDAY"
    :max-life 8
    :ability "Each time you use three or more 1 and/or 2, you also regain 2 life points."}
   {:name "MOLLY STARK"
    :max-life 8
    :ability "Each time another player must lose 1 or more life points, you can lose them in his place."}])
(ns btdg.config)

(def defaults
  {:dice-count 5
   :arrow-count 9
   :renegade-count 2
   :outlaw-count 3
   :deputy-count 2})
(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [active-player-idx player-idx player]
  (let [active-marker (if (= active-player-idx player-idx) ">" " ")]
    (if (-> player :life pos?)
      (let [role (:role player)]
        (println (format "%s[%s] %s (LIFE %d/%d) (ARROWS %d)"
                         active-marker
                         player-idx
                         (-> role name str/upper-case)
                         (-> player :life)
                         (-> player :max-life)
                         (-> player :arrows)))
        (when (#{:sheriff :deputy} role)
          (println (format "     %s: %s"
                           (-> player :name)
                           (-> player :ability)))))
      (println (format "%s[%s] DEAD" active-marker player-idx)))))

(defn- print-die! [die-idx {:keys [new? value]}]
  (let [new-roll-marker (if new? ">" " ")]
    (println (format "%s[%s] %s" new-roll-marker die-idx value))))

(defn print-game! [game]
  (->> game
       (:players)
       (map-indexed (partial print-player! (:active-player-idx game)))
       (dorun))
  (println (format " ARROWS %d" (:arrows game)))
  (println (format " DICE ROLLS %d" (:dice-rolls game)))
  (->> game
       (:dice)
       (map-indexed print-die!)
       (dorun)))
(ns btdg.commands
  (:require [btdg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- get-player-k [game player-idx k]
  (get-in game [:players player-idx k]))

(defn- update-player-k [game player-idx k f & args]
  (apply update-in game [:players player-idx k] f args))

(defn- do-for-players [f game player-idx arg idxs-and-args]
  (reduce (fn [g [player-idx arg]]
             (f g player-idx arg))
           game
           (cons [player-idx arg] (partition 2 idxs-and-args))))

(defn- next-active-player-idx [game]
  (let [n (-> game :players count)]
    (->> (iterate inc 1)
         ;; generate seq of next player idxs
         (map #(-> game :active-player-idx (+ %) (mod n)))
         ;; find live player idxs
         (filter #(-> game (get-player-k % :life) pos?))
         ;; get the next one
         (first))))

(defn- roll-die [die]
  (let [roll (rand/uniform 0 6)]
    (case (:type die)
      :base (case roll
              0 "1"
              1 "2"
              2 "ARROW"
              3 "BEER"
              4 "DYNAMITE"
              5 "GATLING GUN")
      :loudmouth (case roll
                   0 "1 (2)"
                   1 "2 (2)"
                   2 "ARROW"
                   3 "BULLET"
                   4 "DYNAMITE"
                   5 "GATLING GUN (2)")
      :coward (case roll
                0 "1"
                1 "ARROW"
                2 "ARROW (BROKEN)"
                3 "BEER"
                4 "BEER (2)"
                5 "DYNAMITE"))))

;; commands

(defn roll-dice
  ([game]
   (let [n (-> game :dice count)]
     ;; by default, reroll all dice
     (apply roll-dice game (range n))))
  ([game & die-idxs]
   (let [die-idxs (set die-idxs)
         updated-dice (map-indexed (fn [idx die]
                                     (if-not (die-idxs idx)
                                       (dissoc die :new?)
                                       (-> die
                                           (assoc :value (roll-die die))
                                           (assoc :new? true))))
                                   (:dice game))]
     (-> game
         ;; update dice
         (assoc :dice updated-dice)
         ;; increment # dice rolls
         (update :dice-rolls inc)))))

(defn take-arrows
  ([game player-idx]
   ;; by default, take 1 arrow
   (take-arrows game player-idx 1))
  ([game player-idx n]
   (let [;; can't take more than arrows remaining
         arrows (min n (:arrows game))]
     (-> game
         (update :arrows - arrows)
         (update-player-k player-idx :arrows + arrows))))
  ([game player-idx n & args]
   (do-for-players take-arrows game player-idx n args)))

(defn discard-arrows
  ([game player-idx]
   ;; by default, discard all arrows
   (discard-arrows game player-idx (:arrow-count cfg/defaults)))
  ([game player-idx n]
   (let [player-arrows (get-player-k game player-idx :arrows)
         ;; can't discard more than arrows taken
         arrows (min n player-arrows)]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update :arrows + arrows))))
  ([game player-idx n & args]
   (do-for-players discard-arrows game player-idx n args)))

(defn gain-life
  ([game player-idx]
   ;; by default, gain 1 life
   (gain-life game player-idx 1))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         ;; can't go above max life
         add-life #(min (+ %1 %2) max-life)]
     (update-player-k game player-idx :life add-life n)))
  ([game player-idx n & args]
   (do-for-players gain-life game player-idx n args)))

(defn lose-life
  ([game player-idx]
   ;; by default, lose 1 life
   (lose-life game player-idx 1))
  ([game player-idx n]
   (let [;; can't go below 0 life
         remove-life #(max (- %1 %2) 0)
         updated-game (update-player-k game player-idx :life remove-life n)]
     (cond-> updated-game
       ;; if player is dead, discard arrows
       (-> updated-game (get-player-k player-idx :life) zero?)
       (discard-arrows player-idx))))
  ([game player-idx n & args]
   (do-for-players lose-life game player-idx n args)))

(defn indians-attack
  ([game]
   (let [player-idxs (-> game :players count range)]
     ;; by default, attack all players
     (apply indians-attack game player-idxs)))
  ([game & player-idxs]
   (let [idxs-and-arrows (->> player-idxs
                              (map #(get-player-k game % :arrows))
                              (interleave player-idxs))]
     (-> game
         (#(apply lose-life % idxs-and-arrows))
         (#(apply discard-arrows % idxs-and-arrows))))))

(defn gatling-gun
  ([game]
   (let [active-player-idx (:active-player-idx game)
         player-idxs (-> game :players count range)]
     ;; by default, attack all OTHER players
     (apply gatling-gun game (remove #{active-player-idx} player-idxs))))
  ([game & player-idxs]
   (let [idxs-and-hits (interleave player-idxs (repeat 1))]
     (-> game
         (#(apply lose-life % idxs-and-hits))
         (discard-arrows (:active-player-idx game))))))

(defn setup-dice [game]
  (let [active-player-idx (:active-player-idx game)
        base-dice (repeat (:dice-count cfg/defaults) {:type :base})
        dice (case (get-player-k game active-player-idx :name)
               "JOSE DELGADO" (cons {:type :loudmouth} base-dice)
               "TEQUILA JOE"  (cons {:type :coward} base-dice)
               base-dice)]
    (-> game
        ;; setup n dice
        (assoc :dice dice)
        ;; set # dice rolls to 0
        (assoc :dice-rolls 0)
        ;; make first roll
        (roll-dice))))

(defn end-turn [game]
  (-> game
      ;; find next active player
      (assoc :active-player-idx (next-active-player-idx game))
      ;; setup dice
      (setup-dice)))
(ns btdg.setup
  (:require [btdg.characters :as characters]
            [btdg.commands :as cmds]
            [btdg.config :as cfg]
            [repl-games.random :as rand]))

(defn- setup-roles [renegade-count outlaw-count deputy-count]
  (->> (concat (repeat renegade-count :renegade)
               (repeat outlaw-count :outlaw)
               (repeat deputy-count :deputy))
       ;; shuffle non-sheriff roles
       (rand/shuffle*)
       ;; put the sheriff first
       (cons :sheriff)))

(defn- setup-characters [n]
  (let [[x & xs] characters/old-saloon]
    (->> (concat characters/base xs)
         ;; shuffle characters
         (rand/shuffle*)
         ;; take N-1
         (take (dec n))
         ;; put JOSE DELGADO first
         (cons x))))

(defn- setup-players [roles characters]
  (mapv (fn [r c]
          (let [max-life (cond-> (:max-life c)
                           ;; sheriff gets 2 additional life points
                           (= r :sheriff)
                           (+ 2))]
            (-> c
                (assoc :role r)
                (assoc :max-life max-life)
                (assoc :life max-life)
                (assoc :arrows 0))))
        roles
        characters))

(defn mk-game-state [game]
  (let [roles (setup-roles (:renegade-count cfg/defaults)
                           (:outlaw-count cfg/defaults)
                           (:deputy-count cfg/defaults))
        characters (setup-characters (count roles))
        players (setup-players roles characters)]
    (-> game
        ;; setup players
        (assoc :players players)
        ;; make first player active
        (assoc :active-player-idx 0)
        ;; setup arrows
        (assoc :arrows (:arrow-count cfg/defaults))
        ;; setup dice
        (cmds/setup-dice))))
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
   :rd {:doc "(roll dice): [die-idx ...]"
        :fn cmds/roll-dice}
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
(ns dcdbg.cards.core
  (:require [dcdbg.cards.compiled :as compiled])
  (:import [java.util UUID]))

;; helpers

(defn- mk-cards [card-spec]
  (let [n (:copies card-spec 1)]
    (->> (dissoc card-spec :copies)
         (repeat n)
         (map #(assoc % :id (str (UUID/randomUUID)))))))

(defn- get-cards [sets types]
  (let [sets (set sets)
        types (set types)]
    (->> compiled/cards
         (filter #(and (-> % :set sets) (-> % :type types)))
         (mapcat mk-cards))))

;; cards

(def kick
  (mk-cards {:name "Kick"
             :type :super-power
             :cost 3
             :victory 1
             :power 2
             :copies 16}))

(def punch
  (mk-cards {:name "Punch"
             :type :starter
             :cost 0
             :victory 0
             :power 1
             :copies 36}))

(def vulnerability
  (mk-cards {:name "Vulnerability"
             :type :starter
             :cost 0
             :victory 0
             :copies 16}))

(def weakness
  (mk-cards {:name "Weakness"
             :cost 0
             :victory -1
             :text "Weakness cards reduce your score at the end of the game."
             :copies 20}))

(def main-deck-base (get-cards [:base] [:equipment
                                        :hero
                                        :location
                                        :super-power
                                        :villain]))

(def main-deck-crossover-5 (get-cards [:crossover-5] [:equipment
                                                      :hero
                                                      :location
                                                      :super-power
                                                      :villain]))

(def super-hero (get-cards [:base] [:super-hero]))

(def super-villain (get-cards [:crisis-1] [:super-villain]))
(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :super-villain "VI"
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
   :weakness      {:type :stack
                   :facing :down}
   :timer         {:type :stack
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
    (printf "%-2s %-3s %-1s %-1s %-1s %s\n"
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (or (:cost card) "")
            (if (:text card) "T" "")
            (cond
              (:ongoing card) "O"
              (:defense card) "D"
              :else "")
            (or (:power card) "")
            (:name card))))

(defn print-card-details! [card]
  (doseq [k [:name :type :cost :victory :text :attack :ongoing :defense :power]]
    (when-let [x (k card)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile! [{:keys [name cards]} zone-idx]
  (printf "(%2s) %s (%d)\n" zone-idx (mk-header name) (count cards))
  (->> cards
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary! c idx))))
       (dorun)))

(defn print-stack! [{:keys [name cards]} zone-idx]
  (printf "(%2s) %s (%d)\n" zone-idx (mk-header name) (count cards))
  (when (-> cards first :facing (= :up))
    (print-card-summary! (first cards) 0)))

(defn print-game! [{:keys [messages zones]}]
  (->> zones
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
            [dcdbg.config :as cfg]
            [repl-games.random :as rand]))

;; helpers

(defn- flip [cards facing]
  (map #(assoc % :facing facing) cards))

;; setup

(defn- setup-super-villains [n]
  ;; use Ra's Al-Ghul, Crisis Anti-Monitor, and N - 2 randoms
  (let [[x & svs] cards/super-villain
        ys (->> svs butlast rand/shuffle* (take (- n 2)))
        z (last svs)]
    ;; set Ra's Al-Ghul on top, Crisis Anti-Monitor on bottom
    (concat [x] ys [z])))

(defn- setup-main-deck []
  (let [n (-> cards/main-deck-base count (/ 2))
        [xs ys] (->> cards/main-deck-base rand/shuffle* (split-at n))]
    (-> cards/main-deck-crossover-5
        (concat xs)
        (rand/shuffle*)
        (concat ys))))

(defn- setup-super-heroes []
  ;; use The Flash and 1 random
  (let [[x & xs] cards/super-hero]
    [x (-> xs rand/shuffle* first)]))

(defn- setup-deck []
  (let [{:keys [punch-count vulnerability-count]} cfg/defaults]
    (-> (take vulnerability-count cards/vulnerability)
        (concat (take punch-count cards/punch))
        (rand/shuffle*))))

(defn mk-game-state [game]
  (let [{:keys [hand-size line-up-size super-villain-count]} cfg/defaults
        svs (setup-super-villains super-villain-count)
        [line-up main-deck] (split-at line-up-size (setup-main-deck))
        shs (setup-super-heroes)
        [hand deck] (split-at hand-size (setup-deck))
        msgs (conj (->> shs
                        (mapv #(format "TEXT (%s): %s" (:name %) (:text %))))
                   (let [sv (first svs)]
                     (format "ONGOING (%s): %s" (:name sv) (:ongoing sv))))
        zones (for [[name cards] [[:super-villain svs]
                                  [:weakness cards/weakness]
                                  [:timer []]
                                  [:kick cards/kick]
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
                 :cards (vec (flip cards facing))})]
    (-> game
        (assoc :messages msgs)
        (assoc :zones (vec zones))
        ;; flip first super-villain face up
        (assoc-in [:zones 0 :cards 0 :facing] :up)
        ;; store :id of last Line-Up card
        (assoc :last-line-up-id (-> zones (nth 6) :cards last :id)))))
(ns dcdbg.commands
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]
            [dcdbg.print :as print]
            [repl-games.random :as rand]))

;; helpers

(defn- find-zone-index [game zone-name]
  (->> game
       (:zones)
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
    (-> game :zones (nth zone-idx))))

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
    (update-in game [:zones zone-idx :cards] update-fn)))

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

;; end turn

(defn discard-hand [game]
  (let [hand-count (count-cards game :hand)]
    (if (zero? hand-count)
      game
      (move game :hand :discard :top (range hand-count)))))

(defn exec-super-villain-plan [game]
  (let [line-up-count (count-cards game :line-up)
        last-line-up-id (-> game (get-cards :line-up) last :id)]
    (if (-> game :last-line-up-id (not= last-line-up-id))
      game
      (move* game :line-up :destroyed :top (dec line-up-count)))))

(defn advance-timer [game]
  (move* game :weakness :timer :top 0))

(defn refill-line-up [game]
  (let [line-up-count (count-cards game :line-up)
        last-line-up-id (-> game (get-cards :line-up) last :id)]
    (if (>= line-up-count (:line-up-size cfg/defaults))
      (assoc game :last-line-up-id last-line-up-id)
      (let [{:keys [name type attack]} (get-card game :main-deck 0)
            msgs (when (and (#{:hero :villain} type) attack)
                   [(format "ATTACK (%s): %s" name attack)])]
        (-> game
            (update :messages concat msgs)
            (move* :main-deck :line-up :top 0)
            (refill-line-up))))))

(defn flip-super-villain [game]
  (if (-> game (get-card :super-villain 0) :facing (= :up))
    game
    (let [[sv & svs] (get-cards game :super-villain)
          {:keys [name attack ongoing]} sv
          msgs (concat (when attack
                         [(format "ATTACK (%s): %s" name attack)])
                       (when ongoing
                         [(format "ONGOING (%s): %s" name ongoing)]))
          new-svs (-> sv (assoc :facing :up) (cons svs))]
      (-> game
          (update :messages concat msgs)
          (update-cards :super-villain (constantly new-svs))))))

(defn end-turn
  ([game]
   (end-turn game 5))
  ([game n]
   (-> game
       (discard-hand)
       (exec-super-villain-plan)
       (advance-timer)
       (draw n)
       (refill-line-up)
       (flip-super-villain))))
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
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
