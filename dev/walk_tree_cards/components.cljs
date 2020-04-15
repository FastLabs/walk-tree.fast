(ns walk-tree-cards.components
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.text-field :refer [outlined-text-field]]
            [component.button :as btn]
            [component.form-fields :as form]
            [devcards.core]))

(defcard-rg
  text-input-component
  [:div
   [:div
    [:h3 "Filled"]
    [:div {:style {:display :flex :align-items :flex-start :justify-content :space-between :flex-wrap :wrap}}
     [outlined-text-field {:placeholder "Continent"
                           :density     0
                           :help-text   "no value"}]

     [outlined-text-field {:placeholder "Country"
                           :help-text   "has value"
                           :value       "Moldova"}]

     [outlined-text-field {:placeholder "Country"
                           :help-text   "disabled"
                           :disabled?   true
                           :value       "Moldova"}]

     [outlined-text-field {:placeholder "Country"
                           :help-text   "has icon"
                           :value       "Moldova"}]]]])


(defcard-rg
  button-component
  (let [style {:margin "8px 18px"}]
    [:div
     [:div
      [:h3.mdc-typography--subtitle1 "Default"]
      [btn/button {:label "simple"
                   :style style}]
      [btn/button {:label "icon"
                   :style style
                   :icon  "star"}]
      [btn/button {:label   "-1"
                   :style   style
                   :density -1
                   :icon    "star"}]
      [btn/button {:label   "-2"
                   :density -2
                   :style   style
                   :icon    "star"}]
      [btn/button {:label   "-3"
                   :density -3
                   :style   style
                   :icon    "star"}]]

     [:div
      [:h3.mdc-typography--subtitle1 "Raised"]
      [btn/button {:type  :raised
                   :style style
                   :label "raised"}]

      [btn/button {:type  :raised
                   :style style
                   :label "icon"
                   :icon  "star"}]

      [btn/button {:type    :raised
                   :style   style
                   :label   "-1"
                   :density -1
                   :icon    "star"}]
      [btn/button {:type    :raised
                   :style   style
                   :density -2
                   :label   "-2"
                   :icon    "star"}]
      [btn/button {:type    :raised
                   :style   style
                   :density -3
                   :label   "-3"
                   :icon    "star"}]]

     [:div
      [:h3.mdc-typography--subtitle1 "Outlined"]
      [btn/button {:type  :outlined
                   :style style
                   :label "outlined"}]
      [btn/button {:type  :outlined
                   :style style
                   :label "icon"
                   :icon  "star"}]
      [btn/button {:type    :outlined
                   :style   style
                   :label   "-1"
                   :density -1
                   :icon    "star"}]
      [btn/button {:type    :outlined
                   :style   style
                   :label   "-2"
                   :density -2
                   :icon    "star"}]
      [btn/button {:type    :outlined
                   :style   style
                   :label   "-3"
                   :density -3
                   :icon    "star"}]]

     [:div
      [:h3.mdc-typography--subtitle1 "Shaped"]
      [btn/button {:type  :un-elevated
                   :style (merge style {:border-radius 18})
                   :label "shaped"}]

      [btn/button {:type  :un-elevated
                   :style (merge style {:border-radius 18})
                   :icon  "star"
                   :label "shaped"}]

      [btn/button {:type    :un-elevated
                   :style   (merge style {:border-radius 18})
                   :icon    "star"
                   :density -1
                   :label   "-1"}]
      [btn/button {:type    :un-elevated
                   :style   (merge style {:border-radius 18})
                   :icon    "star"
                   :density -2
                   :label   "-1"}]
      [btn/button {:type    :un-elevated
                   :style   (merge style {:border-radius 18})
                   :icon    "star"
                   :density -3
                   :label   "-3"}]]

     [:div
      [:h3.mdc-typography--subtitle1 "Unelevated"]
      [btn/button {:type  :un-elevated
                   :style style
                   :label "un-elevated"}]
      [btn/button {:type  :un-elevated
                   :style style
                   :icon  "star"
                   :label "icon"}]

      [btn/button {:type    :un-elevated
                   :style   style
                   :icon    "star"
                   :density -1
                   :label   "-1"}]
      [btn/button {:type    :un-elevated
                   :style   style
                   :icon    "star"
                   :density -2
                   :label   "-2"}]
      [btn/button {:type    :un-elevated
                   :style   style
                   :icon    "star"
                   :density -3
                   :label   "-3"}]]]))

(defcard-rg
  form-fields
  [:div [form/form-field [form/check-box {:id "one"}] "simple checkbox"]])



