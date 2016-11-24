(ns btdg.setup
  (:require [btdg.cards :as cards]
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
  (map (fn [r c]
         (assoc c
                :role r
                :life (:max-life c)))
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
               :arrows (:arrow-count cfg/defaults)}]
    (assoc game :state state)))
