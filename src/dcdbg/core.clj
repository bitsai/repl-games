(ns dcdbg.core
  (:require [dcdbg.setup :as setup]
            [repl-games.core :as repl-games]))

(def help-atom (atom []))

(def world-atom (atom nil))

(repl-games/mk-help {:ns *ns*
                     :help-atom help-atom})

(repl-games/mk-setup {:ns *ns*
                      :help-atom help-atom
                      :world-atom world-atom
                      :mk-game-state setup/mk-game-state})
