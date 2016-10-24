(ns dcdbg.print
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]))

;; helpers

(defn- mk-header [x]
  (-> x name str/upper-case))

;; printers

(defn print-card-summary [card card-idx]
  (printf "%2s) " card-idx)
  (case (:type card)
    :super-villain
    (printf "%-4s %-1s %-1s %s\n"
            (or (:cost card) "")
            (if (:stack-ongoing card) "G" "")
            (if (:first-appearance-attack card) "A" "")
            (:name card))

    :super-hero
    (println (:name card))

    (printf "%-1s %-2s %-1s %-1s %-2s %s\n"
            (or (:cost card) "")
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (if (:text card) "T" "")
            (cond
              (:attack card) "A"
              (:defense card) "D"
              (:ongoing card) "G"
              :else "")
            (or (:power card) "")
            (:name card))))

(defn print-card-details [card]
  (doseq [k [:name :type :cost :victory :text :power :attack :defense :ongoing :stack-ongoing :first-appearance-attack]]
    (when-let [x (k card)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile [cards space-idx space-name]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header space-name) (count cards))
  (->> cards
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary c idx))))
       (dorun)))

(defn print-stack [cards space-idx space-name]
  (printf "[%2s] %s (%d)\n" space-idx (mk-header space-name) (count cards))
  (when (-> cards first :facing (= :up))
    (print-card-summary (first cards) 0)))

(defn print-game [{:keys [messages state]}]
  (->> state
       (map-indexed (fn [idx [space-name cards]]
                      ;; insert separator between game and player cards
                      (when (= idx 7)
                        (println))
                      (case (-> cfg/card-spaces space-name :type)
                        :pile (print-pile cards idx space-name)
                        :stack (print-stack cards idx space-name))))
       (dorun))
  (when-let [msgs (seq messages)]
    (println)
    (doseq [msg msgs]
      (println msg))))
