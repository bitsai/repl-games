(ns dcdbg.core
  (:require [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def world-atom (atom nil))

(repl-games/mk-setup *ns* world-atom setup/mk-game-state)
