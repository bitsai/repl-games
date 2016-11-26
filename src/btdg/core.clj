(ns btdg.core
  (:require [btdg.commands :as cmds]
            [btdg.print :as print]
            [btdg.setup :as setup]
            [repl-games.core :as repl-games]))

(def game-atom (atom nil))

(def help-atom (atom []))

(def meta-fn-map
  {:mk-game-state setup/mk-game-state
   :print-game print/print-game!})

(def command-map
  (array-map
   :pg {:doc "(print game)"
        :fn print/print-game!}
   :ta {:doc "(take arrows): player-idx [n]"
        :fn cmds/take-arrows}
   :da {:doc "(discard arrows): player-idx [n]"
        :fn cmds/discard-arrows}
   :gl {:doc "(gain life): player-idx [n]"
        :fn cmds/gain-life}
   :ll {:doc "(lose life): player-idx [n]"
        :fn cmds/lose-life}
   :et {:doc "(end turn)"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
