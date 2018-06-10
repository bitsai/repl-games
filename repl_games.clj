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
    :ability "You may take an arrow instead of losing a life point (except to Indians or Jams)."}
   {:name "BLACK JACK"
    :max-life 8
    :ability "You may re-roll \"Jam\" (not if you roll three or more!)."}
   {:name "CALAMITY JANE"
    :max-life 8
    :ability "You can use \"1\" as \"2\" and vice-versa."}
   {:name "EL GRINGO"
    :max-life 7
    :ability "When a player makes you lose one or more life points, he must take an arrow."}
   {:name "JESSE JONES"
    :max-life 9
    :ability "If you have four life points or less, you gain two if you use \"Bandage\" for yourself."}
   {:name "JOURDONNAIS"
    :max-life 7
    :ability "You never lose more than one life point to Indians."}
   {:name "KIT CARLSON"
    :max-life 7
    :ability "For each \"Fan the Hammer\" you may discard one arrow from any player."}
   {:name "LUCKY DUKE"
    :max-life 8
    :ability "You may make one extra re-roll."}
   {:name "PAUL REGRET"
    :max-life 9
    :ability "You never lose life points to Fan the Hammer."}
   {:name "PEDRO RAMIREZ"
    :max-life 8
    :ability "Each time you lose a life point, you may discard one of your arrows."}
   {:name "ROSE DOOLAN"
    :max-life 9
    :ability "You may use \"1\" or \"2\" for players sitting one place further."}
   {:name "SID KETCHUM"
    :max-life 8
    :ability "At the beginning of your turn, any player of your choice gains one life point."}
   {:name "SLAB THE KILLER"
    :max-life 8
    :ability "Once per turn, you can use a \"Bandage\" to double a \"1\" or \"2\"."}
   {:name "SUZY LAFAYETTE"
    :max-life 8
    :ability "If you didn't roll any \"1\" or \"2\", you gain two life points."}
   {:name "VULTURE SAM"
    :max-life 9
    :ability "Each time another player is eliminated, you gain two life points."}
   {:name "WILLY THE KID"
    :max-life 8
    :ability "You only need two \"Fan the Hammer\" to use Fan the Hammer."}])

(def old-saloon
  [{:name "JOSE DELGADO"
    :max-life 7
    :ability "You may use the Loudmouth die without replacing a base die (roll six dice total)."}
   {:name "TEQUILA JOE"
    :max-life 7
    :ability "You may use the Coward die without replacing a base die (roll six dice total)."}
   {:name "APACHE KID"
    :max-life 9
    :ability "If you roll \"Indian arrow\", you may take the Indian Chief's Arrow from another player."}
   {:name "BILL NOFACE"
    :max-life 9
    :ability "Apply \"Indian arrow\" results only after your last roll."}
   {:name "ELENA FUENTE"
    :max-life 7
    :ability "Each time you roll one or more \"Indian arrow\", you may give one of those arrows to a player of your choice."}
   {:name "VERA CUSTER"
    :max-life 7
    :ability "Each time you must lose life points for \"1\" or \"2\", you lose 1 life point less."}
   {:name "DOC HOLYDAY"
    :max-life 8
    :ability "Each time you use three or more \"1\" and/or \"2\", you also regain 2 life points."}
   {:name "MOLLY STARK"
    :max-life 8
    :ability "Each time another player must lose 1 or more life points, you can lose them in his place."}])
(ns btdg.config)

(def defaults
  {:dice-count 5
   :arrow-count 9
   :renegade-count 1
   :outlaw-count 2
   :deputy-count 1})

(def dice
  {:base ["1"
          "2"
          "Bandage"
          "Fan the Hammer"
          "Indian arrow"
          "Jam"]
   :loudmouth ["1 (x2)"
               "2 (x2)"
               "Fan the Hammer (x2)"
               "Indian arrow"
               "Jam"
               "Misfire"]
   :coward ["1"
            "Bandage"
            "Bandage (x2)"
            "Broken arrow"
            "Indian arrow"
            "Jam"]})
(ns btdg.print
  (:require [clojure.string :as str]))

(defn- print-player! [active-player-idx player-idx player]
  (let [active-marker (if (= active-player-idx player-idx) "<" " ")]
    (if (-> player :life pos?)
      (let [role-name (-> player :role name str/upper-case)
            {:keys [name role max-life life arrows]} player]
        (println (format "(%s)%s %-8s %d/%d %d"
                         player-idx
                         active-marker
                         role-name
                         life
                         max-life
                         arrows))
        (when-let [ability (:ability player)]
          (println (format "     %s" ability))))
      (println (format "(%s)%s DEAD" player-idx active-marker)))))

(defn- print-die! [die-idx {:keys [new? value]}]
  (let [new-marker (if new? "<" " ")]
    (println (format "(%s)%s %s" die-idx new-marker value))))

(defn print-game! [game]
  (->> game
       (:players)
       (map-indexed (partial print-player! (:active-player-idx game)))
       (dorun))
  (println (format "ARROWS %d" (:arrows game)))
  (println (format "DICE ROLLS %d" (:dice-rolls game)))
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

(defn- do-for-players [game f player-idxs-and-args]
  (reduce (fn [g [player-idx arg]]
             (f g player-idx arg))
           game
           (partition 2 player-idxs-and-args)))

(defn- next-active-player-idx [game]
  (let [n (-> game :players count)]
    (->> (iterate inc 1)
         (map #(-> game :active-player-idx (+ %) (mod n)))
         (filter #(-> game (get-player-k % :life) pos?))
         (first))))

(defn- roll-die [die]
  (-> die :type cfg/dice rand/rand-nth*))

;; commands

(defn roll-dice [game & die-idxs]
  (let [die-idxs (set die-idxs)
        ;; use mapv to evaluate immediately, to avoid bad randomness
        updated-dice (mapv (fn [idx die]
                             (if-not (die-idxs idx)
                               (dissoc die :new?)
                               (-> die
                                   (assoc :value (roll-die die))
                                   (assoc :new? true))))
                           (range)
                           (:dice game))]
    (-> game
        (assoc :dice updated-dice)
        (update :dice-rolls inc))))

(defn take-arrows
  ([game]
   (take-arrows game (:active-player-idx game) 1))
  ([game n]
   (take-arrows game (:active-player-idx game) n))
  ([game player-idx n]
   (let [;; can't take more than arrows remaining
         arrows (min n (:arrows game))]
     (-> game
         (update :arrows - arrows)
         (update-player-k player-idx :arrows + arrows)))))

(defn discard-arrows
  ([game]
   (discard-arrows game (:active-player-idx game) (:arrow-count cfg/defaults)))
  ([game n]
   (discard-arrows game (:active-player-idx game) n))
  ([game player-idx n]
   (let [player-arrows (get-player-k game player-idx :arrows)
         ;; can't discard more than arrows taken
         arrows (min n player-arrows)]
     (-> game
         (update-player-k player-idx :arrows - arrows)
         (update :arrows + arrows)))))

(defn gain-life
  ([game]
   (gain-life game (:active-player-idx game) 1))
  ([game n]
   (gain-life game (:active-player-idx game) n))
  ([game player-idx n]
   (let [max-life (get-player-k game player-idx :max-life)
         ;; can't go above max life
         add-life #(min (+ %1 %2) max-life)]
     (update-player-k game player-idx :life add-life n)))
  ([game player-idx n & args]
   (do-for-players game gain-life (concat [player-idx n] args))))

(defn lose-life
  ([game]
   (lose-life game (:active-player-idx game) 1))
  ([game n]
   (lose-life game (:active-player-idx game) n))
  ([game player-idx n]
   (let [;; can't go below 0 life
         remove-life #(max (- %1 %2) 0)
         updated-game (update-player-k game player-idx :life remove-life n)]
     (cond-> updated-game
       ;; if player is dead, discard their arrows
       (-> updated-game (get-player-k player-idx :life) zero?)
       (discard-arrows player-idx (:arrow-count cfg/defaults)))))
  ([game player-idx n & args]
   (do-for-players game lose-life (concat [player-idx n] args))))

(defn indians-attack [game & excluded-player-idxs]
  (let [player-idxs (->> (-> game :players count range)
                         (remove (set excluded-player-idxs)))
        player-idxs-and-arrows (->> player-idxs
                                    (map #(get-player-k game % :arrows))
                                    (interleave player-idxs))]
    (-> game
        (do-for-players lose-life player-idxs-and-arrows)
        (do-for-players discard-arrows player-idxs-and-arrows))))

(defn gatling-gun [game & excluded-player-idxs]
  (let [;; always exclude active player
        active-player-idx (:active-player-idx game)
        excluded-player-idxs (cons active-player-idx excluded-player-idxs)
        player-idxs (->> (-> game :players count range)
                         (remove (set excluded-player-idxs)))
        player-idxs-and-hits (interleave player-idxs (repeat 1))]
    (-> game
        (do-for-players lose-life player-idxs-and-hits)
        (discard-arrows))))

(defn setup-dice [game]
  (let [active-player-idx (:active-player-idx game)
        base-dice (repeat (:dice-count cfg/defaults) {:type :base})
        dice (case (get-player-k game active-player-idx :name)
               "JOSE DELGADO" (cons {:type :loudmouth} base-dice)
               "TEQUILA JOE"  (cons {:type :coward} base-dice)
               base-dice)]
    (-> game
        (assoc :dice dice)
        (assoc :dice-rolls 0)
        (#(apply roll-dice % (range (count dice)))))))

(defn end-turn [game]
  (-> game
      (assoc :active-player-idx (next-active-player-idx game))
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
  (->> (concat characters/base characters/old-saloon)
       (rand/shuffle*)
       (take n)))

(defn- setup-players [roles characters]
  (mapv (fn [r c]
          (let [max-life (cond-> (:max-life c)
                           ;; sheriff gets 2 additional life points
                           (= r :sheriff)
                           (+ 2))]
            (-> (cond-> c
                  ;; remove name and ability for renegades and outlaws
                  (#{:renegade :outlaw} r)
                  (dissoc :name :ability))
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
        (assoc :players players)
        (assoc :active-player-idx 0)
        (assoc :arrows (:arrow-count cfg/defaults))
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
   :rd {:doc "(roll dice): die-idx ..."
        :fn cmds/roll-dice}
   :ta {:doc "(take arrows): [n] | player-idx n"
        :fn cmds/take-arrows}
   :da {:doc "(discard arrows): [n] | player-idx n"
        :fn cmds/discard-arrows}
   :gl {:doc "(gain life): [n] | player-idx n ..."
        :fn cmds/gain-life}
   :ll {:doc "(lose life): [n] | player-idx n ..."
        :fn cmds/lose-life}
   :ia {:doc "(Indians attack): excluded-player-idx ..."
        :fn cmds/indians-attack}
   :fh {:doc "(fan the hammer): excluded-player-idx ..."
        :fn cmds/gatling-gun}
   :et {:doc "(end turn)"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
(ns orc-battle.core)

(def ^:dynamic *player-health* (atom nil))
(def ^:dynamic *player-agility* (atom nil))
(def ^:dynamic *player-focus* (atom nil))
(def ^:dynamic *player-attacks* (atom nil))
(def ^:dynamic *player-double-tap-damage* (atom nil))
(def ^:dynamic *monsters* (atom nil))
(def ^:dynamic *monster-builders* (atom nil))
(def monster-num 12)

(declare orc-battle game-loop-start game-loop-end init-player player-dead? show-player player-attack-start player-attack-end s d1 d2 f randval random-monster init-monsters monster-dead? monsters-dead? show-monsters make-monster monster-hit monster-show monster-attack)

(defn orc-battle []
  (init-monsters)
  (init-player)
  (game-loop-start))

(defn game-loop-start []
  (reset! *player-attacks* (inc (quot (max 0 @*player-agility*) 15)))
  (show-player)
  (show-monsters)
  (player-attack-start))

(defn game-loop-end []
  (cond
    (monsters-dead?)
    (println "Congratulations! You have vanquished all of your foes.")

    (pos? @*player-attacks*)
    (do (show-monsters)
        (player-attack-start))

    :else
    (do (doseq [x (range (count @*monsters*))
                :let [m (nth @*monsters* x)]]
          (when-not (monster-dead? m)
            (swap! *monsters* assoc x (monster-attack m))))
        (if (player-dead?)
          (println "You have been killed. Game Over.")
          (game-loop-start)))))

(defn init-player []
  (reset! *player-health* 30)
  (reset! *player-agility* 30)
  (reset! *player-focus* 30))

(defn player-dead? []
  (<= @*player-health* 0))

(defn show-player []
  (printf "You are a valiant gunslinger with a health of %d, an agility of %d, and a focus of %d.\n" @*player-health* @*player-agility* @*player-focus*))

(defn player-attack-start []
  (println "Attack style: [s]nipe [d1|d2]ouble tap [f]an the hammer"))

(defn player-attack-end []
  (swap! *player-attacks* dec)
  (game-loop-end))

(defn s [x]
  (let [y (+ 2 (randval (quot @*player-focus* 2)))]
    (swap! *monsters* update x #(monster-hit % y))
    (player-attack-end)))

(defn d1 [x]
  (let [y (inc (randval (quot @*player-focus* 4)))]
    (reset! *player-double-tap-damage* y)
    (printf "Your double tap has a damage of %d.\n" y)
    (swap! *monsters* update x #(monster-hit % y))
    nil))

(defn d2 [x]
  (swap! *monsters* update x #(monster-hit % @*player-double-tap-damage*))
  (player-attack-end))

(defn f []
  (dotimes [_ (inc (randval (quot @*player-focus* 4)))]
    (when-not (monsters-dead?)
      (swap! *monsters* update (random-monster) #(monster-hit % 2))))
  (player-attack-end))

(defn randval [n]
  (inc (rand-int (max 1 n))))

(defn random-monster []
  (let [x (rand-int (count @*monsters*))]
    (if (monster-dead? (nth @*monsters* x))
      (random-monster)
      x)))

(defn init-monsters []
  (let [;; double parens in the lambda to call the random monster-builder
        monsters (repeatedly monster-num #((rand-nth @*monster-builders*)))]
    (reset! *monsters* (vec monsters))))

(defn monster-dead? [m]
  (<= (:health m) 0))

(defn monsters-dead? []
  (every? monster-dead? @*monsters*))

(defn show-monsters []
  (println "Your foes:")
  (doseq [[x m] (map list (range) @*monsters*)]
    (if (monster-dead? m)
      (printf "   %d. **dead**\n" x)
      (printf "   %d. (Health=%d) %s\n" x (:health m) (monster-show m)))))

(defn make-monster []
  {:health (randval 10)})

(defmulti monster-hit :type)

(defmethod monster-hit :default [m x]
  (let [m (update m :health - x)]
    (if (monster-dead? m)
      (printf "You killed the %s!\n" (:type m))
      (printf "You hit the %s, knocking off %d health points!\n" (:type m) x))
    m))

(defmulti monster-show :type)

(defmethod monster-show :default [m]
  (format "A fierce %s." (:type m)))

(defmulti monster-attack :type)

(defn make-orc []
  (assoc (make-monster) :type "orc" :club-level (randval 8)))

(swap! *monster-builders* conj make-orc)

(defmethod monster-show "orc" [m]
  (format "A wicked orc with a level %d club." (:club-level m)))

(defmethod monster-attack "orc" [m]
  (let [x (randval (:club-level m))]
    (printf "An orc swings his club at you and knocks off %d of your health points.\n" x)
    (swap! *player-health* - x)
    m))

(defn make-hydra []
  (assoc (make-monster) :type "hydra"))

(swap! *monster-builders* conj make-hydra)

(defmethod monster-show "hydra" [m]
  (format "A malicious hydra with %d heads." (:health m)))

(defmethod monster-hit "hydra" [m x]
  (let [m (update m :health - x)]
    (if (monster-dead? m)
      (println "The corpse of the fully decapitated and decapacitated hydra falls to the floor!")
      (printf "You shoot off %d of the hydra's heads!\n" x))
    m))

(defmethod monster-attack "hydra" [m]
  (let [x (randval (quot (:health m) 2))]
    (printf "A hydra attacks you with %d of its heads! It also grows back one more head!\n" x)
    (swap! *player-health* - x)
    (update m :health inc)))

(defn make-slime-mold []
  (assoc (make-monster) :type "slime mold" :sliminess (randval 5)))

(swap! *monster-builders* conj make-slime-mold)

(defmethod monster-show "slime mold" [m]
  (format "A slime mold with a sliminess of %d." (:sliminess m)))

(defmethod monster-attack "slime mold" [m]
  (let [x (randval (:sliminess m))]
    (printf "A slime mold wraps around your arms and decreases your agility by %d!\n" x)
    (swap! *player-agility* - x)
    (when (zero? (rand-int 2))
      (println "It also squirts in your face, taking away a health point!")
      (swap! *player-health* dec))
    m))

(defn make-brigand []
  (assoc (make-monster) :type "brigand"))

(swap! *monster-builders* conj make-brigand)

(defmethod monster-attack "brigand" [m]
  (let [x (max @*player-health* @*player-agility* @*player-focus*)]
    (cond
      (= x @*player-health*)
      (do (println "A brigand hits you with his pistol, taking off 2 health points!")
          (swap! *player-health* - 2))

      (= x @*player-agility*)
      (do (println "A brigand catches your arm with his whip, taking off 2 agility points!")
          (swap! *player-agility* - 2))

      (= x @*player-focus*)
      (do (println "A brigand cuts your head with his whip, taking off 2 focus points!")
          (swap! *player-focus* - 2)))
    m))
