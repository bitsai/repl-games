(ns icrpg.monsters)

(def core
  [{:name "AGNAR"
    :hearts 3
    :rolls "+5 STR, +3 ALL OTHERS"
    :actions-per-turn 1
    :actions {"ATTACK-CHOMP" "Weapon Effort, all CLOSE Enemies"
              "ATTACK-FLYING LEAP" "Leap to Move FAR, then Ultimate Effort against any enemies CLOSE to landing impact"
              "INT SPELL-SPIN-THRASH" "Weapon Effort, all NEAR Enemies, recover 5 HP. NEAR enemies make a DEX Check to half the damage"}}
   {:name "BRAIN HORROR"
    :hearts 2
    :rolls "+3 ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-STINGING TENTACLES" "Weapon Effort, Poisonous (continue to take damage until a CON Check is made)"
              "INT SPELL-BETRAYAL" "All near enemies make an INT Check or turn against a random ally for 1 turn"
              "INT SPELL-PSYCHIC BLAST" "Single target, any distance, Ultimate Effort. If rolling more than 6 Effort on a success, the spell compels its target to walk toward the Brain Horror for 1 turn"
              "INT SPELL-FEED" "CLOSE, Single target, do Magic Damage against a touched enemy, and heal that much HP"}}
   {:name "CORRODER"
    :hearts 3
    :rolls "+2 STATS, +1 WEAPON DAMAGE"
    :actions-per-turn 1
    :actions {"ATTACK-GRABBERS" "Snatch one piece of GEAR from a target, target gets competing STR roll to resist"
              "ATTACK-LASH" "Weapons Effort with sharp claws"}}
   {:name "CRYSTAL WORM"
    :hearts 4
    :rolls "+4 STATS, +2 WEAPON DAMAGE"
    :actions-per-turn 1
    :actions {"ATTACK-MANDIBLES" "Weapon Effort, target rolls a competing STR check or is restrained by the huge jaws"
              "INT SPELL-WEB SHOCK" "All NEAR enemies make a DEX check or take MAGIC DAMAGE as a red shockwave expands outward. Those who fail are also glued where they stand by red tendrils of goo until a STR check is made to break free"}}
   {:name "BLACK DRAKE"
    :hearts 5
    :rolls "+5 ALL STATS AND ROLLS"
    :actions-per-turn "1-3"
    :actions {"ATTACK-CRUSHING BITE" "NEAR Weapon Effort, if target dropped to zero HP by CRUSHING BITE then it is devoured whole"
              "ATTACK-SLASHING CLAWS" "NEAR Weapon Effort, on a 15 or better Attempt roll Ultimate Effort"
              "ATTACK-WHIPPING TAIL" "All NEAR enemies must react with a DEX Check or take weapon damage. Those who fail also fall down, and cannot take a double move on their next turn"
              "ATTACK-BREATH OF FIRE" "All enemies within FAR range, in front of the Drake, must react with a CON save to this attack. Enveloped in fire, even those who succeed take half the DOUBLE ULTIMATE damage done. All flammable objects ignite in drake-fire"
              "INT SPELL-SPELL STEALER" "On a Hard Attempt, the Drake can utilize any INT spell used by its opponents"}}
   {:name "EYE BEAST"
    :hearts 3
    :rolls "+6 STATS, +4 MAGIC EFFECTS"
    :actions-per-turn 2
    :actions {"ATTACK-MAGIC BEAMS" "Roll 2D8 on the Eye Beast's turn, fire those beams at 2 random targets. The Eye Beast can only have as many beams as it has eyes intact. Beams reach FAR range"
              "1-BEAM-DEATH RAY" "Drops its target to 0 HP, can also annihilate any inanimate object it hits"
              "2-BEAM-MAGIC NULLIFIER" "Target cannot use any Magic Effects for 1D4 ROUNDS"
              "3-BEAM-GLUE" "Target is immobilized in their current location, until delivering 10 points of Basic, Weapons or Magic Effort to the sticky strands and globs at its feet"
              "4-BEAM-ENERGY FIST" "Slam a target for Weapon damage, and shove it away from the Eye Beast until impacting a solid surface. IF no solid surface is available, target will be shoved 1D4 x FAR distance away"
              "5-BEAM-GAZE OF STONE" "The victim of this beam is turned to stone for 1D6 hours. If the statue is left unattended with an Eye Beast, it will perform a ritual making the effect permanent"
              "6-BEAM-TERROR" "Target uses its next move getting as far from the Eye Beast as physically possible using any powers items or movement elements available"
              "7-BEAM-DOORWAY" "The Eye Beast gazes into the cosmos, opening a doorway to a dimension of hellish fiends. From this door appear 1D4 proto-goblinoids enslaved to the Eye Beast's will"
              "8-BEAM-SUNDER" "Targets hit with this beam are reduced to 10 total Armor for 1D4 ROUNDS"}}
   {:name "BANSHEE"
    :hearts 2
    :rolls "+2 ALL STATS AND ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-CLAWS" "Spectral Slash, Magic Effort claw attack"
              "INT SPELL-DEAFENING HOWL" "All NEAR enemies must react with a CON check. Those who fail are immobilized and disabled, holding their ears and being plagued by visions of horror. The sound continues until the Banshee is injured or decides to stop on its own"
              "INT SPELL-DRAIN" "With an INT attempt, the Banshee can drain Magic Effort of HP from a target to heal itself"}}
   {:name "BORE WORM"
    :hearts 2
    :rolls "+3 STATS, +3 WEAPON DAMAGE"
    :actions-per-turn 2
    :actions {"ATTACK-STONE LASH" "CLOSE Weapon Effort, can also be used to destroy any inanimate object in a single Attack"
              "ATTACK-STONE BITE" "CLOSE Weapon Effort, if over 8 Effort in one Attack, destroy one item on target"
              "INT SPELL-VENOM SCENT" "Any enemy engaging at close range with a Bore Worm must make a CON check each turn or become dizzy with noxious gas emitted from small holes in the Worm's shell. The dizziness causes 1 Poison Damage, and makes any rolls HARD for that target"}}
   {:name "MINOTAUR BERSERK"
    :hearts 3
    :rolls "+3 STR, +3 WEAPON DAMAGE"
    :actions-per-turn 1
    :actions {"ATTACK-BATTLE AXE" "Weapon Damage, a huge, brutal weapon"
              "ATTACK-CLEAVE" "Bracing and spinning its massive axe, The Berserk hits all CLOSE enemies with a Weapon attack on a single Attempt roll"}}
   {:name "GOBLINS, GERBLINS, & GOBLINGS"
    :hearts 1
    :rolls "+2 STR, +2 DEX"
    :actions-per-turn 1
    :actions {"ATTACK-CRUMMY WEAPON" "Weapon Effort, with equipment no one wants to steal"
              "ATTACK-HOME MADE BOW" "Weapon Effort, cannot shoot beyond FAR distance"}}
   ;; SLIME CUBE
   {:name "BLIND HORROR"
    :hearts 2
    :rolls "+5 DEX, +2 ALL OTHERS"
    :actions-per-turn 1
    :actions {"ATTACK-SPIKED TAIL" "CLOSE Weapon Effort, rolls with DEX to attack, ignores armor (rolls on target rather than hero armor)"
              "ATTACK-FIRE RAY" "Magic Effort, a ranged attack that sears a target within FAR range. Those hit also take 1D4 burn damage on their next turn"
              "WIS POWER-HEAL WITH FLAMES" "The Horror uses its turn to heal itself with Magical Effort"}}
   {:name "FLAMING SKULL"
    :hearts 2
    :rolls "+2 STATS, +4 MAGIC EFFECTS"
    :actions-per-turn 1
    :actions {"INT SPELL-PARALYZE" "A single target is struck motionless without making a HARD INT Check. Paralysis lasts 4 ROUNDS OR until the Check is made"
              "ATTACK-MAGMA BOMB" "These cackling fiends spit a blob of molten fire at a target or area. Anything NEAR the impact site for the next 4 ROUNDS must make a DEX Check or take Magical Fire Damage. Successful Checks result in half damage"}}])
