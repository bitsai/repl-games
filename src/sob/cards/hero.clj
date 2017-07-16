(ns sob.cards.hero)

(def cards
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
    :abilities {"Comforting Presence" "At the end of the Hero Turn may Heal 1 Wound or 1 Sanity from every other adjacent Hero (gain 5 XP for each Wound/Sanity Healed this way)."
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
    :abilities {"Double-Shot (Shotgun)" "Once per turn, when you kill an Enemy with a Shotgun, you cain +1 Shot with that Shotgun."}
    :starting-items ["Shotgun" "US Marshal Badge"]
    :health 10
    :defense 3
    :sanity 10
    :will-power 4}])
