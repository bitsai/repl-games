(ns supercrew.core
  (:require [clojure.string :as str]))

;; https://fortressofsolidrules.wordpress.com/2009/11/12/villain-points-by-blacksheep/

(def avengers
  {:heroes [{:name "Captain America"
             :abilities ["Leadership 3"
                         "Vibranium shield 2"
                         "Super-Soldier 1"]
             :tricks ["[ ] Heroism [re-roll]"
                      "[ ] Reserve strength [Effect +1]"
                      "[ ] Shield throw [Effect 2]"]}
            {:name "Hulk"
             :abilities ["Rage 3"
                         "Super strength 2"
                         "Brawler 1"]
             :tricks ["[ ] Hulk stomp [re-roll]"
                      "[ ] Fury [Effect +1]"
                      "[ ] Hulk smash [Effect 2]"]}
            {:name "Iron Man"
             :abilities ["Alpha strike"
                         "Iron Man armor 2"
                         "J.A.R.V.I.S. 1"]
             :tricks ["[ ] Speed overdrive [re-roll]"
                      "[ ] Energy reroute [Effect +1]"
                      "[ ] Uni-beam [Effect 2]"]}
            {:name "Thor"
             :abilities ["God of Thunder 3"
                         "Mjolnir 2"
                         "Asgardian warrior 1"]
             :tricks ["[ ] God speed [re-roll]"
                      "[ ] Thor's rage [Effect +1]"
                      "[ ] Mjolnir slam [Effect 2]"]}]
   :enemies {1 [{:name "Chitauri"
                 :abilities ["Chitauri gun 1"]
                 :ability-names ["Chitauri bomb"]}]
             2 [{:name "Leviathan"
                 :abilities ["Charge 2"]
                 :ability-names ["Bite"
                                 "Slam"]}]
             3 [{:name "Loki"
                 :abilities ["Scepter 3"]
                 :ability-names ["Daggers"
                                 "Illusions"
                                 "Mind control"]}]}})

(def templates
  {1 {:rank 1
      :toughness 2
      :boosts 1}
   2 {:rank 2
      :toughness 3
      :boosts 2}
   3 {:rank 3
      :toughness 5
      :boosts 3}})

(defn add-ability [{:keys [ability-names rank] :as enemy} ability-fn]
  (let [[an & ans] (shuffle ability-names)]
    (-> enemy
        (update :abilities conj (ability-fn an rank))
        (update :ability-names (constantly ans)))))

(def boost-fns
  [(fn [enemy]
     (add-ability enemy #(format "[ ] %s %d" %1 (inc %2))))
   (fn [enemy]
     (add-ability enemy #(format "[ ] %s %d [gives the target a -1 penalty]" %1 %2)))
   (fn [enemy]
     (add-ability enemy #(format "[ ] %s %d [attacks two targets]" %1 %2)))
   (fn [enemy]
     (add-ability enemy #(format "[ ] %s [provides a two-dice defense]" %1 %2)))
   (fn [enemy]
     (update enemy :toughness + 2))])

(defn init-enemy [enemy template]
  (loop [[bf & bfs] (shuffle boost-fns)
         {:keys [boosts] :as e} (merge enemy template)]
    (if (zero? boosts)
      e
      (recur bfs (-> e bf (update :boosts dec))))))

(defn init-enemies [n enemies]
  (loop [n n
         es []]
    (if (zero? n)
      es
      (let [x (-> (min n 3) rand-int inc)
            e (init-enemy (-> x enemies rand-nth) (templates x))]
        (recur (- n x) (conj es e))))))

(defn print-character [{:keys [abilities name toughness tricks]}]
  (printf "%s\n" name)
  (doseq [x (concat abilities tricks)]
    (printf "%s\n" x))
  (printf "Toughness %d\n" toughness))

(defn print-hero [hero]
  (print-character (assoc hero :toughness 3))
  (println "[ ] Anecdote bonus | Hero points 0"))

(defn ng [{:keys [heroes enemies]}]
  (let [n 2]
    (doseq [e (init-enemies (inc n) enemies)]
      (print-character e)
      (println))
    (println "--\n")
    (doseq [h (->> heroes shuffle (take n))]
      (print-hero h)
      (println))))

(defn r [n]
  (loop [n n
         rs []]
    (let [r (-> 6 rand-int inc)]
      (cond
        (zero? n) (printf "%s => Effect %d" rs (->> rs (filter #(> % 3)) count))
        (= r 6)   (recur n (conj rs r))
        :else     (recur (dec n) (conj rs r))))))
