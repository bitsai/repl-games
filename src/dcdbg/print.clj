(ns dcdbg.print
  (:require [clojure.string :as str]
            [dcdbg.config :as cfg]))

;; helpers

(defn- mk-alias [k]
  (-> cfg/aliases (get k) name))

(defn- mk-header [k]
  (-> k name str/upper-case))

;; printers

(defn print-card-summary [card-idx card]
  (printf "%2s) " (inc card-idx))
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
              (-> card :type mk-alias str/upper-case (subs 0 2))
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
    (when-let [x (get card k)]
      (printf "%s: %s\n" (mk-header k) x))))

(defn print-pile [pile k]
  (printf "[%s] %s (%d)\n" (mk-alias k) (mk-header k) (count pile))
  (->> pile
       (map-indexed (fn [idx c]
                      (when (-> c :facing (= :up))
                        (print-card-summary idx c))))
       (dorun)))

(defn print-stack [stack k]
  (printf "[%s] %s (%d)\n" (mk-alias k) (mk-header k) (count stack))
  (when (-> stack first :facing (= :up))
    (print-card-summary 0 (first stack))))

(defn print-world-state [{:keys [game-state message-queue]}]
  (doseq [[k v] (take 7 cfg/card-spaces)]
    (case (:type v)
      :pile (print-pile (get game-state k) k)
      :stack (print-stack (get game-state k) k)))
  (println)
  (doseq [[k v] (drop 7 cfg/card-spaces)]
    (case (:type v)
      :pile (print-pile (get game-state k) k)
      :stack (print-stack (get game-state k) k)))
  (when-let [msgs (seq message-queue)]
    (println)
    (doseq [msg msgs]
      (println msg))))
