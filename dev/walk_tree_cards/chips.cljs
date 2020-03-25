(ns walk-tree-cards.chips
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [component.chip :as ch]))




(defn chip-bar
  []
  [:div.mdc-chip-set.mdc-chip-set--choice
   [ch/chip {:title "Extra Small"}]
   [ch/chip {:title "Small"}]
   [ch/chip {:title "Medium" :selected? true}]])


(defcard-rg
  chips-card
  [chip-bar])

