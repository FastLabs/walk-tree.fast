(ns walk-tree-cards.tree-view
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [component.tree-view :as tree-view]
            [devcards.core]
            [reagent.core]))


(defcard-rg
  simple-tree
  [:div "Simple Structure"
   [:div [tree-view/tree-view {:family [{:father "Oleg"}]}]]])

(defcard-rg
  searcheable-tree
  [:div "Search Structure"
   [tree-view/searchable-tree {:family {:structure [{:father "Oleg"
                                                     :mother "Lina"}
                                                    {:son "luca"}]}}]])


