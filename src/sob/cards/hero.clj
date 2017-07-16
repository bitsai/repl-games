(ns sob.cards.hero)

(def characters
  [{:class "Bandido"
    :keywords ["Outlaw"]
    :to-hit {:range 5
             :melee 4}
    :combat 2
    :max-grit 2
    :attributes {:agility 2
                 :cunning 1
                 :spirit 3
                 :strength 4
                 :lore 3
                 :luck 2}
    :initiative 3
    :abilities {"Wild Card" "You may Recover a Grit on a Move roll of 1 or 2."}
    :starting-items ["Pistol"]
    :health 16
    :defense 4
    :sanity 8
    :will-power 5}

   {:class "Gunslinger"
    :keywords ["Showman"]
    :to-hit {:range 3
             :melee 5}
    :combat 1
    :max-grit 2
    :attributes {:agility 3
                 :cunning 3
                 :spirit 2
                 :strength 2
                 :lore 2
                 :luck 4}
    :initiative 6
    :abilities {"Quick and the Dead" "Uses the Six Shooter Template. It starts each game fully loaded with 6 Dead Eye Shot bullets."}
    :starting-items ["Pistol"]
    :health 10
    :defense 5
    :sanity 12
    :will-power 4}

   {:class "Saloon Girl"
    :keywords ["Performer"]
    :to-hit {:range 4
             :melee 4}
    :combat 2
    :max-grit 2
    :attributes {:agility 4
                 :cunning 3
                 :spirit 3
                 :strength 1
                 :lore 2
                 :luck 3}
    :initiative 5
    :abilities {"Comforting Presence" "At the end of the turn may Heal 1 Wound or 1 Sanity from every other adjacent Hero (gain 5 XP for each Wound/Sanity Healed this way)."
                "Lightweight" "May only use Guns that have the Keyword Light."
                "Fast" "+1 Move."}
    :starting-items ["Hold-Out Pistol"]
    :health 8
    :defense 3
    :sanity 14
    :will-power 4}

   {:class "U.S. Marshal"
    :keywords ["Law" "Traveler"]
    :to-hit {:range 4
             :melee 4}
    :combat 2
    :max-grit 2
    :attributes {:agility 3
                 :cunning 4
                 :spirit 2
                 :strength 2
                 :lore 1
                 :luck 3}
    :initiative 4
    :abilities {"Double-Shot (Shotgun)" "Once per turn, when you kill an Enemy with a Shotgun, you gain +1 Shot with that Shotgun."}
    :starting-items ["Shotgun" "US Marshal Badge"]
    :health 10
    :defense 3
    :sanity 10
    :will-power 4}])

(def starting-upgrades
  [{:class "Bandido"
    :name "Explosives Expert"
    :text "Use 2 Grit to gain a Dynamite Token."
    :extra-starting-gear ["Dynamite Satchel" "Dynamite Token" "Dynamite Token"]}
   {:class "Bandido"
    :name "Swindler"
    :text "Anytime you draw a Loot card, you may discard it and draw a new one. You must keep the second card drawn. You are also +1 Combat."}
   {:class "Bandido"
    :name "Twin Guns"
    :text "You may fire two 1 Hand Icon Guns per turn with no penalty for the off-hand Gun."
    :extra-starting-gear ["Pistol"]}

   {:class "Gunslinger"
    :name "Pistol Fanning"
    :text "Use 1 Grit to double the number of Shots you get with a 1 Hand Icon Gun for one Attack. (Limit once per turn). To use this ability, you must have 1 Hand Icon open."}
   {:class "Gunslinger"
    :name "Quickdraw"
    :text "During the first turn of a Fight, you are Initiative 10 and may gain +1 Shot with a 1 Hand Icon Gun."}
   {:class "Gunslinger"
    :name "Reload"
    :text "Use 2 Grit to re-fill D6 Shots back into your Six Shooter Template."}

   {:class "Saloon Girl"
    :name "Acrobatic Dodge"
    :text "You may move through other models during your movement. Once per turn you may Re-roll one failed Defense roll."}
   {:class "Saloon Girl"
    :name "Dirty Fightin'"
    :text "All of your Attacks are +1 Damage."}
   {:class "Saloon Girl"
    :name "Knockout Punch"
    :text "Use 1 Grit to double the amount just rolled on one of your Damage rolls. (Limit once per Hit)."}

   {:class "U.S. Marshal"
    :name "Cleaning Up The West"
    :text "Any time you kill an Enemy you may Heal 1 Wound and 1 Sanity, and gain 10 XP."}
   {:class "U.S. Marshal"
    :name "Hardened Resolve"
    :text "Use 1 Grit to Heal 3 Wounds or 3 Sanity from yourself or another Hero on your Map Tile (gain 5 XP for every Wound/Sanity Healed from another Hero in this way). You are +2 Sanity."}
   {:class "U.S. Marshal"
    :name "Rolling Thunder"
    :text "Anytime you kill an Enemy, you may Recover a Grit on the D6 roll of 4, 5, or 6."}])
