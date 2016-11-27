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
