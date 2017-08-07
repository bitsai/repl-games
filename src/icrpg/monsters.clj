(ns icrpg.monsters)

(def core
  [{:name "AGNAR"
    :hearts 3
    :rolls "+5 STR, +3 ALL OTHERS"
    :actions-per-turn 1
    :actions {"ATTACK-CHOMP" "Weapon Effort, all CLOSE Enemies"
              "LIE IN WAIT" "Disguised as a green rock, free surprise Chomp attack on CLOSE passers-by"
              "ATTACK-FLYING LEAP" "Leap to Move FAR, then Ultimate Effort against any enemies CLOSE to landing impact"
              "INT SPELL-SPIN-THRASH" "Weapon Effort, all NEAR Enemies, recover 5 HP. NEAR enemies make a DEX Check to half the damage"}}
   {:name "BRAIN HORROR"
    :hearts 2
    :rolls "+3 ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-STINGING TENTACLES" "Weapon Effort, Poisonous (continue to take damage until a CON Check is made)"
              "LEVITATE" "Does not need terrain to move, always in flight, can rise up to FAR height"
              "INT SPELL-BETRAYAL" "All near enemies make an INT Check or turn against a random ally for 1 turn"
              "INT SPELL-PSYCHIC BLAST" "Single target, any distance, Ultimate Effort. If rolling more than 6 Effort on a success, the spell compels its target to walk toward the Brain Horror for 1 turn"
              "INT SPELL-FEED" "CLOSE, Single target, do Magic Damage against a touched enemy, and heal that much HP"}}
   {:name "CORRODER"
    :hearts 3
    :rolls "+2 STATS, +1 WEAPON DAMAGE"
    :actions-per-turn 1
    :actions {"ATTACK-GRABBERS" "Snatch one piece of GEAR from a target, target gets competing STR roll to resist"
              "CHOMP" "As an instant action, the Corroder rolls Weapons Effort as it chomps on stolen gear. Rolls over 5 destroy the gear instantly, rolls under 5 damage the gear. For any piece eaten whole, the Corroder returns to full HEARTS"
              "ATTACK-LASH" "Weapons Effort with sharp claws"
              "CLIMB" "Corroders can take their MOVE actions on any surface"}}
   {:name "CRYSTAL WORM"
    :hearts 4
    :rolls "+4 STATS, +2 WEAPON DAMAGE"
    :actions-per-turn 1
    :actions {"ATTACK-MANDIBLES" "Weapon Effort, target rolls a competing STR check or is restrained by the huge jaws"
              "INT SPELL-WEB SHOCK" "All NEAR enemies make a DEX check or take MAGIC DAMAGE as a red shockwave expands outward. Those who fail are also glued where they stand by red tendrils of goo until a STR check is made to break free"
              "PSIONIC CALL" "After the first round of combat, a Crystal Worm will vibrate its antennae, emittng a silent pulse. 1D4 more worms will answer this call in 1D4 ROUNDS"}}
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
              "8-BEAM-SUNDER" "Targets hit with this beam are reduced to 10 total Armor for 1D4 ROUNDS"
              "LEVITATE" "Does not need terrain to move, always in flight, can rise up to FAR height"}}
   {:name "BANSHEE"
    :hearts 2
    :rolls "+2 ALL STATS AND ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-CLAWS" "Spectral Slash, Magic Effort claw attack"
              "INT SPELL-DEAFENING HOWL" "All NEAR enemies must react with a CON check. Those who fail are immobilized and disabled, holding their ears and being plagued by visions of horror. The sound continues until the Banshee is injured or decides to stop on its own"
              "BLINK" "Banshees can move up to a FAR distance without traveling between"
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
              "HEADLONG CHARGE" "Rushing with unbridled fury, a Minotaur Berserk moves one FAR move in a straight line at a target. Anything between his origin and his destination is smashed, all enemies must use DEX to evade the charge or take an Ultimate Weapon impact from the monster"
              "ATTACK-CLEAVE" "Bracing and spinning its massive axe, The Berserk hits all CLOSE enemies with a Weapon attack on a single Attempt roll"}}
   {:name "GOBLINS, GERBLINS, & GOBLINGS"
    :hearts 1
    :rolls "+2 STR, +2 DEX"
    :actions-per-turn 1
    :actions {"ATTACK-CRUMMY WEAPON" "Weapon Effort, with equipment no one wants to steal"
              "ATTACK-HOME MADE BOW" "Weapon Effort, cannot shoot beyond FAR distance"
              "YELL FOR HELP" "A terrified Gerblin will yell for aid. On his next turn, even if dead by then, 1D4 Goblin friends will arrive"
              "FLEE AND HIDE" "Run away! Goblings can hide almost anywhere. If they reach a hidey hole, they vanish and recover full HP"}}
   {:name "SLIME CUBE"
    :hearts 3
    :rolls "+5 STR"
    :actions-per-turn 1
    :actions {"MOVE" "The Slime Cube uses its Action to move to a NEAR destination. It will choose a random direction or move toward sound and light. It never hurries"
              "IMMOBILE" "Some Cubes are commanded or formed to be in a single place and never move"
              "STICKY ACIDIC ABSORPTION" "Anything besides stone that touches a Slime Cube becomes stuck to it. In 1D4 ROUNDS, if the object or creature has not escaped, it is absorbed into the Cube's interior. Stuck creatures must roll a higher STR roll than the Cube to pull free. Up to 6 creatures can be stuck at once"
              "ABSORBED" "Once absorbed, creatures and objects take Ultimate Acid Damage for each turn inside. Regardless of HEARTS, any living thing trapped inside suffocates in 4 ROUNDS. This kind of death ignores a normal 'DYING' roll. The ONLY way out is for the Cube to be destroyed"}}
   {:name "BLIND HORROR"
    :hearts 2
    :rolls "+5 DEX, +2 ALL OTHERS"
    :actions-per-turn 1
    :actions {"ATTACK-SPIKED TAIL" "CLOSE Weapon Effort, rolls with DEX to attack, ignores armor (rolls on target rather than hero armor)"
              "ATTACK-FIRE RAY" "Magic Effort, a ranged attack that sears a target within FAR range. Those hit also take 1D4 burn damage on their next turn"
              "WIS POWER-HEAL WITH FLAMES" "The Horror uses its turn to heal itself with Magical Effort"
              "GATHER POWER" "A Blind Horror can use an Action to gather its power, doubling any Effort done on its next turn"}}
   {:name "FLAMING SKULL"
    :hearts 2
    :rolls "+2 STATS, +4 MAGIC EFFECTS"
    :actions-per-turn 1
    :actions {"LEVITATE" "Does not need terrain to move, always in flight, can rise up to FAR height"
              "INT SPELL-PARALYZE" "A single target is struck motionless without making a HARD INT Check. Paralysis lasts 4 ROUNDS OR until the Check is made"
              "FIRE SWORD" "On its turn, the Skull can conjure a blazing sword which will engage combatants on its own. The sword is spectral, and cannot be harmed. It fights with normal Attempts and Weapon Effort, and vanishes only when the Skull is killed or flees"
              "ATTACK-MAGMA BOMB" "These cackling fiends spit a blob of molten fire at a target or area. Anything NEAR the impact site for the next 4 ROUNDS must make a DEX Check or take Magical Fire Damage. Successful Checks result in half damage"}}
   {:name "GARGOLETH"
    :hearts 6
    :rolls "+8 STATS, +8 WEAPON DAMAGE"
    :actions-per-turn "1-3"
    :actions {"ATTACK-TENTACLE" "FAR Weapon Effort, hits all targets NEAR a point of attack. Targets there must make a DEX check to evade. Those who fail roll again, a 10 or less results in being grabbed by the tentacle. Inflicts 1D4 ship damage"
              "ATTACK-CRUSHING BEAK" "Ultimate NEAR Weapon Effort on a single target. If target reduced to zero HP, it is devoured whole"
              "WAVE SURGE" "A Gargoleth can convulse its massive body to form a huge wave. The wave is as large as the average ship, and almost as high. All creatures in its path must make a HARD STR check or be swept up. Those who succeed hold on, but take 1D6 damage"
              "ATTACK-STORM OF SPINES" "Using compressed air, a cloud of many barbed spines fly out of the creature's skin. There is no way to avoid them unless sheltered by wood, steel or stone. Those NEAR the Gargoleth all take 3 Weapon damage instantly"}}
   {:name "GIANT TENTACLE"
    :hearts 2
    :rolls "+4 ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-LASH" "Normal Weapon attack, NEAR reach"
              "ATTACK-GRAPPLE" "Without LASHING, the Tentacle wraps itself around a single victim, initiating competing STR rolls. Those overcome are squeezed and must check with CON or be winded, losing their next turn"
              "DRAG DOWN" "Grappled victims are pulled into the depths. The Tentacle will waggle and waiver for 1D4 ROUNDS before doing so"
              "HURL" "If a Tentacle has a victim grappled, and takes any type of damage, it will HURL the victim for a FAR location. The landing, if not mitigated with a DEX check, will cause at least 1D4 damage, depending on the landing site"}}
   {:name "SKELETON"
    :hearts 1
    :rolls "+2 ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-WEAPON" "Weapon Effort, on any critical fail their weapon breaks and they must resort to BASIC attacks"
              "ATTACK-DEATH TOUCH" "Making a normal STR attack, the Skeleton grasps bare skin, burning it with cold blue fire. This causes Magic Damage"}}
   {:name "FLIMES"
    :hearts 1
    :rolls "FOR EACH FLIME BEYOND 1, ALL PRESENT FLIMES GAIN +1 TO ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-BITE" "CLOSE Weapon Effort"
              "ORBIT" "If 3 or more Flimes are CLOSE to one another, they can use their Action to ORBIT. They spin around one another for 1D4 ROUNDS. At the end of that time, they emit a beam of white energy, striking any targets in a straight line for Ultimate Magical Damage"}}
   {:name "CARRION CRAB"
    :hearts 3
    :rolls "+2 STATS, +2 WEAPON DAMAGE"
    :actions-per-turn 2
    :actions {"ATTACK-GIANT CLAWS" "Weapon attack, CLOSE"
              "DIG IN" "As an Action, the Carrion Crab pulls its legs inward and grips the ground. In this state is utterly immovable by any physical means"
              "PASSIVE SPINES AND SPIKES" "These beasts are covered in armor, swords and spears as well as natural chitin spines. Any successful melee attack against them inflicts Weapon Damage on the attacker"}}
   {:name "CAVE ROPER"
    :hearts 2
    :rolls "+4 ALL ROLLS"
    :actions-per-turn 1
    :actions {"ATTACK-LASH" "Normal Weapon attack, NEAR reach, a barbed tongue whips and flays"
              "ATTACK-GRAPPLE" "A single NEAR target can resist with a HARD STR roll or be dragged in and held as food"
              "CHEW" "With no roll, the Cave Roper chews on its held prey as an action. Victim can use a HARD CON check to reduce the Weapon Damage to half with sheer will"}}
   {:name "CHILD OF AZATOTH"
    :hearts 3
    :rolls "+8 ALL ROLLS"
    :actions-per-turn "1 PER VICTIM"
    :actions {"INT SPELL-CONFUSION" "All intelligent creatures within FAR range make ALL rolls as HARD"
              "INT SPELL-SPELL STEALER" "On an Attempt, the THING can utilize any INT spell used by its opponents"
              "AURA OF DECAY" "For each turn spent within NEAR range of the Child of Azatoth, creatures must make a INT check. Failure costs 1 STAT point, chosen at random"
              "INT SPELL-TELEKINESIS" "This being can move objects, even huge ones with an INT check. These are its only physical weapons"
              "INT SPELL-BECKON" "With a competing INT roll against its victim, the Child of Azatoth compels a target to walk slowly toward it for 1D4 ROUNDS. The check can be attempted again each of those turns to break free"
              "INT SPELL-MEMORY EATER" "This spell targets a single victim, and robs them of one key memory from their recent or distant past, whichever is more sadistic"
              "AURA OF OFFERINGS" "Any creature within CLOSE range must make a CHA check. If they fail, they must offer one piece of LOOT to the dead God as tribute"}}
   {:name "SHADOW LASHER"
    :hearts 2
    :rolls "+5 DEX, +2 ALL OTHERS"
    :actions-per-turn 2
    :actions {"ATTACK-LASH" "Weapon Effort, NEAR reach"
              "ATTACK-POUNCE" "The Lasher leaps onto its prey, pinning it down with STR. If Lashed when pinned in this way, the target takes Ultimate Weapon Damage, and gains no benefit from armor"
              "BLINK" "Shadow Lashers can move NEAR without passing between locations"}}
   {:name "OGRE"
    :hearts 4
    :rolls "+8 STR, -3 INT -3 CHA, +3 ALL OTHERS"
    :actions-per-turn 2
    :actions {"ATTACK-SPIKED CLUB" "Weapon Damage with a NEAR reach. Even on a missed Attack, the spiked club inflicts 3 Damage on its target"
              "ATTACK-BODY SLAM" "Ogres hurl their massive bodies at an enemy to crush it. A DEX check is needed to evade when this Attack succeeds. Failure results in DOUBLE Weapon Damage and being immobilized under the creature. Success avoids all effects"
              "ATTACK-SEISMIC HAMMER" "A ground-shaking area Attack. All NEAR enemies must make a CON check or be shaken senseless and lose their next turn"
              "BATTLE CRY" "If cornered and almost dead, an Ogre will call for aid. Roll 1D4, on a 4 1 more Ogre appears nearby"}}
   {:name "TREE OF DEATH"
    :hearts 5
    :rolls "NONE"
    :actions-per-turn 2
    :actions {"CLOUD OF SEEDS" "Anything with FAR range must roll 1D8 as they approach. In that many ROUNDS, they will breathe too many seeds, and will then take 5 Poison damage every turn thereafter"
              "ROOT CORPSE" "The tree pushes animated corpses up with its roots. These will attack anything nearby as 1 HEART monsters. 1D4 spawn at a time"
              "CLOUD OF FEAR" "Any creatures within NEAR range are wracked with visions of hellish agony. Make a WIS or CHA check to resist, otherwise make a FAR move in a random direction"
              "SPAWN INSECTS" "If cut or chopped, the tree will spew out a swarm of biting insects. This swarm can not be harmed with any conventional Weapon, and does Weapon Damage to one target per turn with no avoidance"
              "BLIGHT" "If agitated, the tree will issue forth bursts of subsurface poison. This will taint water and soil for 1 mile in any direction for 1D4 years"
              "CLOUD OF BETRAYAL" "If below 2 HEARTS, the tree cracks. In its core a cloud of spores rests. All creatures must make a HARD CON check or turn against their allies for 1D4 ROUNDS"}}])
