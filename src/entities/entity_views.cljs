(ns entities.entity-views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [component.param-panel :refer [params-panel]]
            [clojure.string :as str]
            [component.app-bar :as app-bar]
            [component.tree-view :as tree-w]
            [entities.loader :as entity-loader]))


(defn entity-title [id entity-name params {:keys [status swap-fn dispose-fn] :as context}]
  [:div
   (let [param-path (->> (filter :mandatory? params)
                         (map :id)
                         (map #(get context %))
                         (str/join "/"))
         title      (if (= status :final) (str entity-name "/" param-path) entity-name)]
     [app-bar/content-bar {:title   title
                           :actions [{:label     "Edit"
                                      :icon      "edit"
                                      :id        (str "edit-" id)
                                      :on-action (fn [_] (swap-fn))}
                                     {:label     "Dispose"
                                      :icon      "close"
                                      :id        (str "delete-" id)
                                      :on-action (fn [_] (dispose-fn id))}]}])])


(defn data-view [data]
  [:div {:style {:margin 4}}
   [tree-w/tree-view data {:on-value-click #(prn %1 %2)}]])

(defn config-view [context entity-loader all-loaders on-context-change]
  (let [loader          (get all-loaders entity-loader)
        default-context (entity-loader/default-loader-context loader)]
    [:div
     {:style {:margin 4}}
     [params-panel (:params loader) (merge default-context context {:entity-loader entity-loader}) on-context-change]]))

(defn entity-view [{:keys [id data entity-id context entity-loader on-param-change on-entity-dispose] :as entity-data} all-loaders]
  ;TODO: understand why i need to status here?
  (let [state   (r/atom {:status (:status context)})
        swap-fn #(swap! state assoc :status :draft)]
    (fn []
      [:div.mdc-card.demo-basic-with-header
       [:div
        [entity-title id entity-id
         (get-in all-loaders [entity-loader :params])
         (assoc context
           :dispose-fn on-entity-dispose
           :swap-fn swap-fn)]]
       (if (= :final (:status @state))
         [data-view data]
         [config-view context entity-loader all-loaders #(-> (swap! state merge (assoc % :status :final))
                                                             (merge {:status :loading
                                                                     :id     id})
                                                             (on-param-change))])])))
