(ns btdg.config)

(def defaults
  {:dice-count 5
   :arrow-count 9
   :renegade-count 1
   :outlaw-count 2
   :deputy-count 1})

(def dice
  {:base ["1" "2" "ARROW" "BEER" "DYNAMITE" "GATLING"]
   :loudmouth ["1 (2)" "2 (2)" "ARROW" "BULLET" "DYNAMITE" "GATLING (2)"]
   :coward ["1" "ARROW" "ARROW (BROKEN)" "BEER" "BEER (2)" "DYNAMITE"]})
