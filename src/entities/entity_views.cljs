(ns entities.entity-views
  (:require [re-frame.core :as ra]
            [reagent.core :as r]
            [component.param-panel :refer [params-panel]]
            [clojure.string :as str]
            [component.app-bar :as app-bar]
            [component.tree-view :as tree-w]))


(defn entity-title [entity-name params {:keys [status swap-fn] :as context}]
  [:div
   (let [param-path (->> (filter :mandatory? params)
                         (map :id)
                         (map #(get context %))
                         (str/join "/"))
         title      (if (= status :final) (str entity-name "/" param-path) entity-name)]
     [app-bar/content-bar {:title   title
                           :actions [{:label     "Edit Entity"
                                      :icon      "edit"
                                      :id        (str "edit-" entity-name)
                                      :on-action (fn [_] (swap-fn))}]}])])


(defn data-view [data]
  [:div {:style {:margin 4}}
   [tree-w/tree-view data]])

(defn config-view [params context on-context-change]
  [:div
   {:style {:margin 4}}
   [params-panel params context on-context-change]])

(defn entity-view [{:keys [data entity-name context params on-param-change]}]
  ;TODO: understand why i need to status here?
  (let [state   (r/atom {:status (:status context)})
        swap-fn #(swap! state assoc :status :draft)]
    (fn []
      [:div.mdc-card.demo-basic-with-header
       [:div
        [entity-title entity-name params (assoc context :swap-fn swap-fn)]]
       (if (= :final (:status @state))
         [data-view data]
         [config-view params context #(-> (swap! state merge (assoc % :status :final))
                                          (dissoc :status)
                                          (on-param-change))])])))
