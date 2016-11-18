# dcdbg

A Solitaire variant of the DC Deck-Building Game, using the base game + Crisis Expansion 1.

- Setup as normal, with the following exceptions:
  - Choose 2 Super Heroes of your choice
  - Use 6 Impossible Super-Villains (Ra's al Ghul, 4 random ones, and Crisis Anti-Monitor)
  - Use the 20 Weakness cards as the Timer stack (the Weakness stack starts empty)
- Play your turn as normal, with the following exception:
  - Before a Super-Villain can be defeated, you must defeat all Villains in the Line-Up
  - When you buy or defeat a card, you may destroy it instead
  - Effects that reveal cards from a foe's deck or hand use the top main deck cards instead
- End of turn sequence:
  - Discard and draw as normal
  - Add the top card of the main deck to the Line-Up and resolve any attack
  - If the top card of the Super-Villain stack is face down, flip it up and resolve any attack
  - Move the top card of the Timer stack to the Weakness stack
- End game:
  - If the Super-Villain stack is emptied first, you win!
  - If the Timer stack is emptied first, you lose!
