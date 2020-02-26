(ns walk-tree-cards.components
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.text-field :as tf]
            [devcards.core]))

(defcard-rg
  text-input-component
  [:div [tf/outlined-text-field {:placeholder "Continent"}]])

