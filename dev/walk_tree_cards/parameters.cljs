(ns walk-tree-cards.parameters
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.param-panel :as pp]
            [component.text-field :as tf]
            [devcards.core]))

(defcard-rg
  parameter-panel
  (let [params [{:title "Continent"
                 :id    :continent
                 :type  :text}
                {:title "Country"
                 :id   :country
                 :type :text}]]
    [:div
     [pp/params-panel {:params params} {:continent "Eurasia"
                                        :country "Moldova"}]]))


