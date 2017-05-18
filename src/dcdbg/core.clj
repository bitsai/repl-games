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
   :mt {:doc "(move to top): from to card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from to card-idx+"
        :fn #(apply cmds/move %1 %2 %3 :bottom %&)}
   :dsv {:doc "(defeat super-villain)"
         :fn cmds/defeat-super-villain}
   :gw {:doc "(gain weakness)"
        :fn #(cmds/move %1 :weakness :discard :top 0)}
   :gk {:doc "(gain kick)"
        :fn #(cmds/move %1 :kick :discard :top 0)}
   :buy {:doc "(buy): card-idx+"
         :fn #(apply cmds/move %1 :line-up :discard :top %&)}
   :dv {:doc "(defeat villain): card-idx+"
        :fn #(apply cmds/move %1 :line-up :destroyed :top %&)}
   :rl {:doc "(refill line-up)"
        :fn cmds/refill-line-up}
   :di {:doc "(discard): card-idx+"
        :fn #(apply cmds/move %1 :hand :discard :top %&)}
   :de {:doc "(destroy): card-idx+"
        :fn #(apply cmds/move %1 :hand :destroyed :top %&)}
   :dw {:dos "(destroy weakness): card-idx+"
        :fn #(apply cmds/move %1 :hand :weakness :top %&)}
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
