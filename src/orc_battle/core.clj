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
  (println "Attack style: [a]imed shot [d1|d2]ouble tap [f]an the hammer"))

(defn player-attack-end []
  (swap! *player-attacks* dec)
  (game-loop-end))

(defn a [x]
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
      (do (println "A brigand catches your arm with his knife, taking off 2 agility points!")
          (swap! *player-agility* - 2))

      (= x @*player-focus*)
      (do (println "A brigand cuts your head with his knife, taking off 2 focus points!")
          (swap! *player-focus* - 2)))
    m))
