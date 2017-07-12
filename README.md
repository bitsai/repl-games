# repl-games

A simple framework for text-based games in the Clojure REPL.

## Notes

Clojure REPL on Android: https://www.deepbluelambda.org/programs/clojure-repl/

command language UI: https://www.snellman.net/blog/archive/2014-12-08-command-languages-as-game-ui/

use "log replay" system for easy undo (and optional features like planning/preview, full history edits, trivial PBEM)

use seeded PRNG, to ensure consistent random outputs when replaying

don't need history of game states, just replay command log to re-compute as needed

when a command is entered: call function, compute new game state, log command + args

let invalid commands crash before they get logged

for simplicity, use "tabletop simulator" approach

## License

Copyright © 2016 Benny Tsai

Distributed under the Eclipse Public License version 1.0.
