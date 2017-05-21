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
   :mt {:doc "(move to top): from-idx to-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-idx to-idx card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :gw {:doc "(gain weakness)"
        :fn #(cmds/move %1 :weakness :discard :top 0)}
   :gk {:doc "(gain kick)"
        :fn #(cmds/move %1 :kick :discard :top 0)}
   :bl {:doc "(buy line-up): card-idx+"
        :fn #(apply cmds/move %1 :line-up :discard :top %&)}
   :rl {:doc "(refill line-up)"
        :fn cmds/refill-line-up}
   :pl {:doc "(play location): card-idx+"
        :fn #(apply cmds/move %1 :hand :location :bottom %&)}
   :di {:doc "(discard): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 :discard :top %&)}
   :de {:doc "(destroy): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 :destroyed :top %&)}
   :dw {:doc "(destroy weakness): space-idx card-idx+"
        :fn #(apply cmds/move %1 %2 :weakness :top %&)}
   :rd {:doc "(refill deck)"
        :fn cmds/refill-deck}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
