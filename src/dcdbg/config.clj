(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :super-villain "VI"
   :villain     "VI"})

(def defaults
  {:hand-size 5
   :line-up-size 5
   :punch-count 7
   :super-villain-count 8
   :vulnerability-count 3})

(def zones
  (array-map
   :super-villain {:type :stack
                   :facing :down}
   :weakness      {:type :stack
                   :facing :down}
   :timer         {:type :stack
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
