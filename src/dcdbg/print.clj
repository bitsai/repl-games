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
