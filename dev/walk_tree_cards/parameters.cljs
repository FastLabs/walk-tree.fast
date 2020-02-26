(ns walk-tree-cards.parameters
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.text-field :as tf]
            [devcards.core]))

(defcard-rg
  parameter-panel
  [:div [tf/outlined-text-field {:placeholder "Continent"}]])

