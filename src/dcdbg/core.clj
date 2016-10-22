(ns dcdbg.core
  (:require [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def help-atom (atom []))

(def world-atom (atom nil))

(def command-map
  {:mk-game-state setup/mk-game-state})

(repl-games/mk-help-setup-undo {:ns *ns*
                                :help-atom help-atom
                                :world-atom world-atom
                                :command-map command-map})
