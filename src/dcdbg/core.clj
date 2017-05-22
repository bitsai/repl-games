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
   :pg {:doc "(print game): [zone-* [card-idx+]]"
        :fn cmds/print!}
   :mt {:doc "(move to top): from-* to-* [card-idx+]"
        :fn #(cmds/move %1 %2 %3 :top %&)}
   :mb {:doc "(move to bottom): from-* to-* [card-idx+]"
        :fn #(cmds/move %1 %2 %3 :bottom %&)}
   :ga {:doc "(gain): zone-* [n]"
        :fn cmds/gain}
   :bl {:doc "(buy line-up): [card-idx+]"
        :fn #(cmds/move %1 :line-up :discard :top %&)}
   :pl {:doc "(play location): [card-idx+]"
        :fn #(cmds/move %1 :hand :location :bottom %&)}
   :di {:doc "(discard): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :discard :top %&)}
   :de {:doc "(destroy): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :destroyed :top %&)}
   :dw {:doc "(destroy weakness): zone-* [card-idx+]"
        :fn #(cmds/move %1 %2 :weakness :top %&)}
   :dr {:doc "(draw): [n]"
        :fn cmds/draw}
   :et {:doc "(end turn): [n]"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
