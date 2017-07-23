(ns sob.cards.hero)

(def characters
  {:bandido {:class "Bandido"
             :to-hit {:range 5
                      :melee 4}
             :combat 2
             :max-grit 2
             :initiative 3
             :abilities {"Wild Card" "You may Recover a Grit on a Move roll of 1 or 2."}
             :starting-items [:pistol]
             :health 16
             :defense 4
             :sanity 8
             :will-power 5}
   :gunslinger {:class "Gunslinger"
                :to-hit {:range 3
                         :melee 5}
                :combat 1
                :max-grit 2
                :initiative 6
                :abilities {"Quick and the Dead" "Uses the Six Shooter Template. It starts each game fully loaded with 6 Dead Eye Shot bullets."}
                :starting-items [:pistol]
                :health 10
                :defense 5
                :sanity 12
                :will-power 4}
   :saloon-girl {:class "Saloon Girl"
                 :to-hit {:range 4
                          :melee 4}
                 :combat 2
                 :max-grit 2
                 :initiative 5
                 :abilities {"Comforting Presence" "At the end of the turn may Heal 1 Wound or 1 Sanity from every other adjacent Hero."}
                 :starting-items [:hold-out-pistol]
                 :health 8
                 :defense 3
                 :sanity 14
                 :will-power 4}
   :us-marshal {:class "U.S. Marshal"
                :to-hit {:range 4
                         :melee 4}
                :combat 2
                :max-grit 2
                :initiative 4
                :abilities {"Double-Shot (Shotgun)" "Once per turn, when you kill an Enemy with a Shotgun, you gain +1 Shot with that Shotgun."}
                :starting-items [:shotgun :marshal-badge]
                :health 10
                :defense 3
                :sanity 10
                :will-power 4}})

(def starting-gear
  {:dynamite-satchel {:name "Dynamite Satchel"
                      :text "Holds up to 5 Dynamite Tokens."}
   :hold-out-pistol {:name "Hold-Out Pistol"
                     :shots 1
                     :text "Free Attack (Once per Fight). Critical Hit on 5 or 6."}
   :marshal-badge {:name "Marshal Badge"
                   :text "Once per Adventure, give all Heroes +2 Shots with a Gun or +2 Combat (they choose) during their next Activation."}
   :pistol {:name "Pistol"
            :shots 2}
   :shotgun {:name "Shotgun"
             :shots 1
             :text "Uses the D8 To Hit and for Damage (6, 7, or 8 count as a Critical Hit)."}})

(def starting-upgrades
  {:bandido [{:name "Explosives Expert"
              :text "Use 2 Grit to gain a Dynamite Token."
              :extra-starting-gear [:dynamite-satchel :dynamite :dynamite]}
             {:name "Swindler"
              :text "You are +1 Combat."}
             {:name "Twin Guns"
              :text "You may fire two Guns per turn with no penalty for the off-hand Gun."
              :extra-starting-gear [:pistol]}]
   :gunslinger [{:name "Pistol Fanning"
                 :text "Use 1 Grit to double the number of Shots you get with a Gun for one Attack. (Limit once per turn)."}
                {:name "Quickdraw"
                 :text "During the first turn of a Fight, you are Initiative 10 and may gain +1 Shot with a Gun."}
                {:name "Reload"
                 :text "Use 2 Grit to re-fill D6 Shots back into your Six Shooter Template."}]
   :saloon-girl [{:name "Acrobatic Dodge"
                  :text "Once per turn you may Re-roll one failed Defense roll."}
                 {:name "Dirty Fightin'"
                  :text "All of your Attacks are +1 Damage."}
                 {:name "Knockout Punch"
                  :text "Use 1 Grit to double the amount just rolled on one of your Damage rolls. (Limit once per Hit)."}]
   :us-marshal [{:name "Cleaning Up The West"
                 :text "Any time you kill an Enemy you may Heal 1 Wound and 1 Sanity."}
                {:name "Hardened Resolve"
                 :text "Use 1 Grit to Heal 3 Wounds or 3 Sanity from yourself or another Hero. You are +2 Sanity."}
                {:name "Rolling Thunder"
                 :text "Anytime you kill an Enemy, you may Recover a Grit on the D6 roll of 4, 5, or 6."}]})

(def tokens
  {:dead-eye-shot {:name "Dead Eye Shot"
                   :text "Discard a Dead Eye Shot Token to add +2 Damage to one of your Ranged Hits with a Gun. You may not use more than one Dead Eye Shot Token per Hit."}
   :dynamite {:name "Dynamite"
              :text "Discard to throw as a Ranged Attack. If missed To Hit roll, bounces D3 times before exploding. Models in the target space and adjacent take D6 Wounds each, ignoring Defense."}})
