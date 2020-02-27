(ns walk-tree-cards.components
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.text-field :as tf]
            [devcards.core]))

(defcard-rg
  text-input-component
  [:div
   [:div
    [:h3 "Filled"]
    [:div {:style {:display :flex :align-items :flex-start :justify-content :space-between :flex-wrap :wrap}}
     [tf/outlined-text-field {:placeholder "Continent"
                              :help-text   "no value"}]

     [tf/outlined-text-field {:placeholder "Country"
                              :help-text   "has value"
                              :value       "Moldova"}]

     [tf/outlined-text-field {:placeholder "Country"
                              :help-text   "disabled"
                              :disabled?   true
                              :value       "Moldova"}]

     [tf/outlined-text-field {:placeholder "Country"
                              :help-text   "has icon"
                              :value       "Moldova"}]]]])


