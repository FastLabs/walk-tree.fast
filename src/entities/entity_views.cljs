(ns entities.entity-views
  (:require [re-frame.core :as ra]
            [component.param-panel :refer [params-panel]]
            [component.tree-view :as tree-w]))

(defn entity-view [{:keys [data entity-name] :as entity}]
  [:div.mdc-card.demo-basic-with-header {:style {:padding 4}}
   [:div.demo-card__primary
    [:h3.demo-card__title.mdc-typography.mdc-typography--headline6 entity-name]
    [params-panel entity]]
   [:div [tree-w/tree-view data]]])