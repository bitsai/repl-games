(ns dcdbg.core
  (:require [dcdbg.commands :as cmds]
            [dcdbg.print :as print]
            [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def game-atom (atom nil))

(def help-atom (atom []))

(def meta-fn-map
  {:mk-game-state setup/mk-game-state
   :print-game print/print-game!})

(def command-map
  (array-map
   :pg {:doc "(print game): [space-idx [card-idx+]]"
        :fn cmds/print!}
   :pt {:doc "(print top): space-idx"
        :fn #(cmds/print! %1 %2 0)}
   :mt {:doc "(move to top): from-space-idx to-space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-space-idx to-space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :pl {:doc "(play location): card-idx+"
        :fn #(apply cmds/move %1 9 8 :bottom %&)}
   :bl {:doc "(buy line-up): card-idx+"
        :fn #(apply cmds/move %1 6 11 :top %&)}
   :ga {:doc "(gain): space-idx [n]"
        :fn cmds/gain}
   :di {:doc "(discard): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 11 :top %&)}
   :de {:doc "(destroy): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 4 :top %&)}
   :rd {:doc "(refill deck)"
        :fn cmds/refill-deck}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
