(ns icrpg.characters)

(def warp-shell
  [{:class "Tank"
    :bio-form "Mecha"
    :stats {:str 7 :dex 0 :con 0 :int 0 :wis 0 :cha 0}
    :armor 13
    :effort {:basic 0 :weapons 2 :magic 0 :ultimate 0}
    :hearts 2
    :gear ["Weapon Kit (+2 Weapon Effort)"
           "Duranium Blade"
           "Common Shield (+2 Armor, can be sacrificed to absorb ALL of one hit against you and be destroyed)"
           "Common Armor (+1 Armor)"
           "Force Shield Module (Ignore any damage of 3 or less)"
           "Impervium Plating (Make a STR Attempt to shrug off a single attack)"]}
   {:class "Gunner"
    :bio-form "Geno"
    :stats {:str 0 :dex 7 :con 0 :int 0 :wis 0 :cha 0}
    :armor 11
    :effort {:basic 0 :weapons 3 :magic 0 :ultimate 0}
    :hearts 1
    :gear ["Weapon Kit (+2 Weapon Effort)"
           "Blast Pistol (6 capacity)"
           "Blast Pistol (6 capacity)"
           "Common Armor (+1 Armor)"
           "Burst Fire Unit (Choose one weapon, on a hit roll Effort three times, distribute between targets as desired)"
           "Slam Loader (Reload a weapon in zero time)"]}
   {:class "Zurin"
    :bio-form "Geno"
    :stats {:str 0 :dex 0 :con 0 :int 4 :wis 4 :cha 0}
    :armor 11
    :effort {:basic 0 :weapons 0 :magic 1 :ultimate 0}
    :hearts 1
    :gear ["Fire Stone (+1 Magic Effort)"
           "Walking Stick"
           "Energy Bow"
           "Common Armor (+1 Armor)"
           "WIS Spell: Healing Words (Attempt with WIS, heal with Magical Effort, must be heard to function)"
           "INT Spell: Words Beyond Time (Alles ALL roll Ultimate Effort on their next success)"]}])
