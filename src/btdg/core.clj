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
   :rd {:doc "(roll dice): die-idx ..."
        :fn cmds/roll-dice}
   :ta {:doc "(take arrows): [n] | player-idx n"
        :fn cmds/take-arrows}
   :da {:doc "(discard arrows): [n] | player-idx n"
        :fn cmds/discard-arrows}
   :gl {:doc "(gain life): [n] | player-idx n ..."
        :fn cmds/gain-life}
   :ll {:doc "(lose life): [n] | player-idx n ..."
        :fn cmds/lose-life}
   :ia {:doc "(Indians attack): [player-idx ...]"
        :fn cmds/indians-attack}
   :fh {:doc "(Fan the Hammer): [player-idx ...]"
        :fn cmds/gatling-gun}
   :et {:doc "(end turn)"
        :fn cmds/end-turn}))

(repl-games/mk-commands! *ns*
                         game-atom
                         help-atom
                         meta-fn-map
                         command-map)
