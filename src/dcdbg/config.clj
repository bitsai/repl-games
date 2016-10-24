(ns dcdbg.config)

(def aliases
  {:equipment   "EQ"
   :hero        "HE"
   :location    "LO"
   :starter     "ST"
   :super-power "SP"
   :villain     "VI"})

(def card-spaces
  (array-map
   :super-villain {:facing :down
                   :type :stack}
   :timer         {:facing :down
                   :type :stack}
   :weakness      {:facing :down
                   :type :stack}
   :kick          {:facing :up
                   :type :stack}
   :destroyed     {:facing :down
                   :type :pile}
   :main-deck     {:facing :down
                   :type :stack}
   :line-up       {:facing :up
                   :type :pile}
   :super-hero {:facing :up
                :type :pile}
   :location   {:facing :up
                :type :pile}
   :hand       {:facing :up
                :type :pile}
   :deck       {:facing :down
                :type :stack}
   :discard    {:facing :down
                :type :pile}))

(def defaults
  {:punch-count 7
   :super-villain-count 8
   :vulnerability-count 3})
