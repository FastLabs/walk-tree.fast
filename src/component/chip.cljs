(ns component.chip
  (:require [component.utils :refer [toggle-class]]))

(defn chip [{:keys [title selected?]}]
  (let [attrs {:role "row"
               :class-name "mdc-chip"}]

    [:div
     (toggle-class attrs #(true? selected?) "mdc-chip--selected")
     [:div.mdc-chip__ripple]
     [:span.mdc-chip__text
      {:role "button"} title]]))
