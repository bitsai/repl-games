(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :villain     "VI"})

(def defaults
  {:hand-size 5
   :line-up-size 5
   :punch-count 7
   :super-villain-count 4
   :vulnerability-count 3})

(def spaces
  (array-map
   :super-villain {:type :stack
                   :facing :down}
   :countdown     {:type :stack
                   :facing :down}
   :weakness      {:type :stack
                   :facing :up}
   :kick          {:type :stack
                   :facing :up}
   :destroyed     {:type :pile
                   :facing :down}
   :main-deck     {:type :stack
                   :facing :down}
   :line-up       {:type :pile
                   :facing :up}
   :super-hero {:type :pile
                :facing :up}
   :location   {:type :pile
                :facing :up}
   :hand       {:type :pile
                :facing :up}
   :deck       {:type :stack
                :facing :down}
   :discard    {:type :pile
                :facing :down}))
