(ns icrpg.monsters)

(def core
  [{:name "Agnar"
    :hearts 3
    :rolls "+5 STR, +3 All Others"
    :actions-per-turn 1
    :actions {"Chomp" "Weapon Effort, all CLOSE Enemies."
              "Flying Leap" "Leap to Move FAR, then Ultimate Effort against any enemies CLOSE to landing impact."
              "INT Spell: Spin-Thrash" "Weapon Effort, all NEAR Enemies, recover 5 HP. NEAR enemies make a DEX Check to half the damage."}}
   {:name "Brain Horror"
    :hearts 2
    :rolls "+3 All Rolls"
    :actions-per-turn 1
    :actions {"Stinging Tentacles" "Weapon Effort, Poisonous (continue to take damage until a CON Check is made)."
              "INT Spell: Betrayal" "All near enemies make an INT Check or turn against a random ally for 1 turn."
              "INT Spell: Psychic Blast" "Single target, any distance, Ultimate Effort. If rolling more than 6 Effort on a success, the spell compels its target to walk toward the Brain Horror for 1 turn."
              "INT Spell: Feed" "CLOSE, Single target, do Magic Damage against a touched enemy, and heal that much HP."}}])
