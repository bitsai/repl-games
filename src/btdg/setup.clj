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
