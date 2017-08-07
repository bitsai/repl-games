(ns icrpg.characters)

(def warp-shell
  [{:name "REINHARDT"
    :class "TANK"
    :bio-form "MECHA"
    :stats {:str 7 :dex 0 :con 0 :int 0 :wis 0 :cha 0}
    :armor 13
    :effort {:basic 0 :weapons 2 :magic 0 :ultimate 0}
    :hearts 2
    :gear {"Weapon Kit" "+2 Weapon Effort"
           "Hammer" nil
           "Common Shield" "+2 Armor (can be sacrificed to absorb ALL of one hit against you and be destroyed)"
           "Common Armor" "+1 Armor"
           "Force Shield Module" "Ignore any damage of 3 or less"}}
   {:name "MCCREE"
    :class "GUNNER"
    :bio-form "GENO"
    :stats {:str 0 :dex 7 :con 0 :int 0 :wis 0 :cha 0}
    :armor 11
    :effort {:basic 0 :weapons 3 :magic 0 :ultimate 0}
    :hearts 1
    :gear {"Weapon Kit" "+2 Weapon Effort"
           "Gauss Pistol" "6 capacity"
           "Common Armor" "+1 Armor"
           "Burst Fire Unit" "Choose one weapon and modify it, on a hit roll Effort three times, distribute between targets as desired (note: if this weapon is lost or destroyed, the Burst Fire Unit is also lost)"}}
   {:NAME "MERCY"
    :class "ZURIN"
    :bio-form "GENO"
    :stats {:str 0 :dex 0 :con 0 :int 0 :wis 7 :cha 0}
    :armor 11
    :effort {:basic 0 :weapons 0 :magic 2 :ultimate 0}
    :hearts 1
    :gear {"Fire Stone" "+1 Magic Effort"
           "Blast Pistol" "6 capacity"
           "Common Armor" "+1 Armor"
           "WIS Power: Healing Words" "Attempt with WIS, heal with Magical Effort, must be heard to function"}}])
