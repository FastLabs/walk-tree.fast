(ns walk-tree-cards.parameters
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [reagent.core :as ra]
            [component.param-panel :as pp]
            [component.text-field :as tf]
            [entities.entity-views :as ew]
            [devcards.core]))

(defcard-rg
  entity-title
  (let [entity {:entity-name "Cities"
                :params      [{:title      "Country"
                               :type       :text
                               :mandatory? true
                               :id         :country}]
                :context     {:country "Moldova"
                              :status  :final}}]



    [:div
     [ew/entity-title (:entity-name entity) (:params entity) (:context entity)]]))



(defcard-rg
  parameter-panel
  (let [params [{:title    "Continent"
                 :param-id :continent
                 :type     :text}
                {:title    "Country"
                 :param-id :country
                 :type     :text}]]
    [:div
     [pp/params-panel params {:continent "Eurasia"
                              :country   "Moldova"}]]))

(defcard-rg
  draft-entity-view
  (let [entity {:data        {:cities ["Chisinau" "Cahul" "Cantemir" "Balti" "Soroca"]}
                :params      [{:title    "Country"
                               :param-id :country
                               :type     :text}]
                :context     {:country "Moldova"
                              :status  :draft}
                :entity-name "Cities"}]
    [:div [ew/entity-view entity]]))

(defcard-rg
  final-entity-view
  (let [entity {:data        {:cities ["Chisinau" "Cahul" "Cantemir" "Balti" "Soroca"]}
                :params      [{:title      "Country"
                               :param-id   :country
                               :mandatory? true
                               :type       :text}]
                :context     {:country "Moldova"
                              :status  :final}
                :entity-name "Cities"}]
    [:div
     [ew/entity-view entity]]))

