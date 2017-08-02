(ns icrpg.monsters)

(def core
  [{:name "Agnar"
    :hearts 3
    :rolls "+5 STR, +3 All Others"
    :actions-per-turn 1
    :actions {"Attack: Chomp" "Weapon Effort, all CLOSE Enemies."
              "Attack: Flying Leap" "Leap to Move FAR, then Ultimate Effort against any enemies CLOSE to landing impact."
              "INT Spell: Spin-Thrash" "Weapon Effort, all NEAR Enemies, recover 5 HP. NEAR enemies make a DEX Check to half the damage."}}
   {:name "Brain Horror"
    :hearts 2
    :rolls "+3 All Rolls"
    :actions-per-turn 1
    :actions {"Attack: Stinging Tentacles" "Weapon Effort, Poisonous (continue to take damage until a CON Check is made)."
              "INT Spell: Betrayal" "All near enemies make an INT Check or turn against a random ally for 1 turn."
              "INT Spell: Psychic Blast" "Single target, any distance, Ultimate Effort. If rolling more than 6 Effort on a success, the spell compels its target to walk toward the Brain Horror for 1 turn."
              "INT Spell: Feed" "CLOSE, Single target, do Magic Damage against a touched enemy, and heal that much HP."}}
   {:name "Corroder"
    :hearts 3
    :rolls "+2 Stats, +1 Weapon Damage"
    :actions-per-turn 1
    :actions {"Attack: Grabbers" "Snatch one piece of GEAR from a target, target gets competing STR roll to resist."
              "Chomp" "As an instant action, the Corroder rolls Weapons Effort as it chomps on stolen gear. Rolls over 5 destroy the gear instantly, rolls under 5 damage the gear. For any piece eaten whole, the Corroder returns to full HEARTS."
              "Attack: Lash" "Weapons Effort with sharp claws."}}
   {:name "Crystal Worm"
    :hearts 4
    :rolls "+4 Stats, +2 Weapon Damage"
    :actions-per-turn 1
    :actions {"Attack: Mandibles" "Weapon Effort, target rolls a competing STR check or is restrained by the huge jaws."
              "INT Spell: Web Shock" "All NEAR enemies make a DEX check or take MAGIC DAMAGE as a red shockwave expands outward. Those who fail are also glued where they stand by red tendrils of goo until a STR check is made to break free."
              "Psionic Call" "After the first round of combat, a Crystal Worm will vibrate its antennae, emittng a silent pulse. 1D4 more worms will answer this call in 1D4 ROUNDS."}}
   {:name "Black Drake"
    :hearts 5
    :rolls "+5 All Stats and Rolls"
    :actions-per-turn "1-3"
    :actions {"Attack: Crushing Bite" "NEAR Weapon Effort, if target dropped to zero HP by CRUSHING BITE then it is devoured whole."
              "Attack: Slashing Claws" "NEAR Weapon Effort, on a 15 or better Attempt roll Ultimate Effort."
              "Attack: Whipping Tail" "All NEAR enemies must react with a DEX Check or take weapon damage. Those who fail also fall down, and cannot take a double move on their next turn."
              "Attack: Breath of Fire" "All enemies within FAR range, in front of the Drake, must react with a CON save to this attack. Enveloped in fire, even those who succeed take half the DOUBLE ULTIMATE damage done. All flammable objects ignite in drake-fire."
              "INT Spell: Spell Stealer" "On a Hard Attempt, the Drake can utilize any INT spell used by its opponents."}}
   {:name "Eye Beast"
    :hearts 3
    :rolls "+6 Stats, +4 Magic Effects"
    :actions-per-turn 2
    :actions {"Attack: Magic Beams" "Roll 2D8 on the Eye Beast's turn, fire those beams at 2 random targets. The Eye Beast can only have as many beams as it has eyes intact. Beams reach FAR range."
              "1 - Beam: Death Ray" "Drops its target to 0 HP, can also annihilate any inanimate object it hits."
              "2 - Beam: Magic Nullifier" "Target cannot use any Magic Effects for 1D4 ROUNDS."
              "3 - Beam: Glue" "Target is immobilized in their current location, until delivering 10 points of Basic, Weapons or Magic Effort to the sticky strands and globs at its feet."
              "4 - Beam: Energy Fist" "Slam a target for Weapon damage, and shove it away from the Eye Beast until impacting a solid surface. IF no solid surface is available, target will be shoved 1D4 x FAR distance away."
              "5 - Beam: Gaze of Stone" "The victim of this beam is turned to stone for 1D6 hours. If the statue is left unattended with an Eye Beast, it will perform a ritual making the effect permanent."
              "6 - Beam: Terror" "Target uses its next move getting as far from the Eye Beast as physically possible using any powers items or movement elements available."
              "7 - Beam: Doorway" "The Eye Beast gazes into the cosmos, opening a doorway to a dimension of hellish fiends. From this door appear 1D4 proto-goblinoids enslaved to the Eye Beast's will."
              "8 - Beam: Sunder" "Targets hit with this beam are reduced to 10 total Armor for 1D4 ROUNDS."}}
   {:name "Banshee"
    :hearts 2
    :rolls "+2 All Stats and Rolls"
    :actions-per-turn 1
    :actions {"Attack: Claws" "Spectral Slash, Magic Effort claw attack."
              "INT Spell: Deafening Howl" "All NEAR enemies must react with a CON check. Those who fail are immobilized and disabled, holding their ears and being plagued by visions of horror. The sound continues until the Banshee is injured or decides to stop on its own."
              "INT Spell: Drain" "With an INT attempt, the Banshee can drain Magic Effort of HP from a target to heal itself."}}
   {:name "Bore Worm"
    :hearts 2
    :rolls "+3 Stats, +3 Weapon Damage"
    :actions-per-turn 2
    :actions {"Attack: Stone Lash" "CLOSE Weapon Effort, can also be used to destroy any inanimate object in a single Attack."
              "Attack: Stone Bite" "CLOSE Weapon Effort, if over 8 Effort in one Attack, destroy one item on target."
              "INT Spell: Venom Scent" "Any enemy engaging at close range with a Bore Worm must make a CON check each turn or become dizzy with noxious gas emitted from small holes in the Worm's shell. The dizziness causes 1 Poison Damage, and makes any rolls HARD for that target."}}])
