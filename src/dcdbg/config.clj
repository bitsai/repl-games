(ns dcdbg.config)

(def defaults
  {:punch-count 7
   :super-villain-count 8
   :vulnerability-count 3})

(def card-spaces
  (array-map
   :super-villain {:alias :sv
                   :facing :down
                   :type :stack}
   :timer         {:alias :ti
                   :facing :down
                   :type :stack}
   :weakness      {:alias :we
                   :facing :down
                   :type :stack}
   :kick          {:alias :ki
                   :facing :up
                   :type :stack}
   :destroyed     {:alias :de
                   :facing :down
                   :type :pile}
   :main-deck     {:alias :md
                   :facing :down
                   :type :stack}
   :line-up       {:alias :lu
                   :facing :up
                   :type :pile}
   :super-hero {:alias :sh
                :facing :up
                :type :pile}
   :location   {:alias :lo
                :facing :up
                :type :pile}
   :hand       {:alias :ha
                :facing :up
                :type :pile}
   :deck       {:alias :dk
                :facing :down
                :type :stack}
   :discard    {:alias :di
                :facing :down
                :type :pile}))

(def card-types
  {:equipment   {:alias :eq}
   :hero        {:alias :he}
   :location    {:alias :lo}
   :starter     {:alias :st}
   :super-power {:alias :sp}
   :villain     {:alias :vi}})

(def aliases
  (->> (for [[k v] (merge card-spaces card-types)]
         [k (:alias v)])
       (into {})))
