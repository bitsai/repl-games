#!/bin/bash

cat src/repl_games/random.clj \
    src/repl_games/core.clj \
    src/btdg/characters.clj \
    src/btdg/config.clj \
    src/btdg/print.clj \
    src/btdg/commands.clj \
    src/btdg/setup.clj \
    src/btdg/core.clj \
    src/dcdbg/cards/compiled.clj \
    src/dcdbg/cards/core.clj \
    src/dcdbg/config.clj \
    src/dcdbg/print.clj \
    src/dcdbg/setup.clj \
    src/dcdbg/commands.clj \
    src/dcdbg/core.clj \
    > repl_games.clj
