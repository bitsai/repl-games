#!/bin/bash

cat src/repl_games/random.clj \
    src/repl_games/core.clj \
    src/btdg/characters.clj \
    src/btdg/config.clj \
    src/btdg/print.clj \
    src/btdg/commands.clj \
    src/btdg/setup.clj \
    src/btdg/core.clj \
    src/icrpg/characters.clj \
    src/icrpg/monsters.clj \
    > repl_games.clj
