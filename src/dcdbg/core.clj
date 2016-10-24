(ns dcdbg.core
  (:require [dcdbg.commands :as commands]
            [dcdbg.print :as print]
            [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def help-atom (atom []))

(def game-atom (atom nil))

(def command-map
  {:mk-game-state setup/mk-game-state
   :pr-game print/print-game
   :pg {:doc "(print game): [space-idx [card-idx+]]"
        :fn commands/print*}})

(repl-games/mk-help-setup-undo {:ns *ns*
                                :help-atom help-atom
                                :game-atom game-atom
                                :command-map command-map})
