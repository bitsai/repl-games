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
    (printf "%-2s %-3s %-1s %-2s %-1s %s\n"
            (if (:type card)
              (-> card :type cfg/aliases)
              "")
            (or (:cost card) "")
            (cond
              (:defense card) "D"
              (:ongoing card) "O"
              :else "")
            (or (:power card) "")
            (if (:text card) "T" "")
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
