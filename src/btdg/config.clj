(ns btdg.config)

(def defaults
  {:dice-count 5
   :arrow-count 9
   :renegade-count 1
   :outlaw-count 2
   :deputy-count 1})

(def dice
  {:base ["1"
          "2"
          "Bandage"
          "Fan the Hammer"
          "Indian arrow"
          "Jam"]
   :loudmouth ["1 (x2)"
               "2 (x2)"
               "Fan the Hammer (x2)"
               "Indian arrow"
               "Jam"
               "Misfire"]
   :coward ["1"
            "Bandage"
            "Bandage (x2)"
            "Broken arrow"
            "Indian arrow"
            "Jam"]})
