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
   :pg {:doc "(print game): [space [card-idx+]]"
        :fn cmds/print!}
   :pt {:doc "(print top): space"
        :fn #(cmds/print! %1 %2 0)}
   :mt {:doc "(move to top): from-space to-space card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-space to-space card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :pl {:doc "(play location): card-idx+"
        :fn #(apply cmds/move %1 :hand :location :bottom %&)}
   :bl {:doc "(buy line-up): card-idx+"
        :fn #(apply cmds/move %1 :line-up :discard :top %&)}
   :ga {:doc "(gain): space [n]"
        :fn cmds/gain}
   :di {:doc "(discard): space card-idx+"
        :fn #(apply cmds/move %1 %2 :discard :top %&)}
   :de {:doc "(destroy): space card-idx+"
        :fn #(apply cmds/move %1 %2 :destroyed :top %&)}
   :rd {:doc "(refill deck)"
        :fn cmds/refill-deck}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :rl {:doc "(refill line-up)"
        :fn #(cmds/move %1 :main-deck :line-up :top 0)}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
