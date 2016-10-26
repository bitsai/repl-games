(ns dcdbg.core
  (:require [dcdbg.commands :as commands]
            [dcdbg.print :as print]
            [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def game-atom (atom nil))

(def help-atom (atom []))

(def command-map
  {:pg {:doc "(print game): [space-idx [card-idx+]]"
        :fn commands/print*}
   :mt {:doc "(move to top): from-space-idx to-space-idx card-idx+"
        :fn #(apply commands/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-space-idx to-space-idx card-idx+"
        :fn #(apply commands/move %1 %2 %3 :bottom %&)}})

(repl-games/mk-commands *ns*
                        game-atom
                        help-atom
                        command-map
                        setup/mk-game-state
                        print/print-game)
