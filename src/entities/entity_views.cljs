(ns entities.entity-views
  (:require [re-frame.core :as ra]
            [component.tree-view :as tree-w]
            [component.text-field :refer [outlined-text-field]]))


;TODO: should be as multi for edit mode: view or edit

(defmulti param-editor :type :default :text)

(defmethod param-editor :text [{:keys [name]}]
  [outlined-text-field {:placeholder name}])




(defmulti params-panel (fn [entity] :draft) :default :draft)


(defmethod params-panel :draft [{:keys [params]}]
  [:div {:style {:display :flex :margin-bottom 20}}
   (for [param params]
     ^{:key param} [:div {:style {:margin-right 20}}
                    [param-editor param]])
   [:div [:button.mdc-button {:on-click #(prn "Request entity")} "GET"]]])


(defn entity-view [{:keys [data entity-name] :as entity}]
  [:div.mdc-card.demo-basic-with-header {:style {:padding 4}}
   [:div.demo-card__primary
    [:h3.demo-card__title.mdc-typography.mdc-typography--headline6 entity-name]
    [params-panel entity]]
   [:div [tree-w/tree-view data]]])