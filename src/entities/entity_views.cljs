(ns entities.entity-views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [component.param-panel :refer [params-panel]]
            [clojure.string :as str]
            [component.app-bar :as app-bar]
            [component.tree-view :as tree-w]
            [entities.loader :as entity-loader]
            [entities.martians :as martian]))

(defn resource-title [loader-id context]
  (martian/get-path loader-id context))

;TODO: review this function this was the first version for the title, could be more shorter than uri, but could be hard to represent as unique id
(defn short-title [entity-name {:keys [params] :as loader} {:keys [status] :as context}]
  (let [param-path (->> (filter :mandatory? params)
                        (map :id)
                        (map #(get context %))
                        (str/join "/"))]
    (if (= status :final) (str entity-name "/" param-path) entity-name)))


(defn entity-title [id loader-id {:keys [swap-fn dispose-fn] :as context}]
  [:div
   [app-bar/content-bar {:title   (resource-title loader-id context)
                         :actions [{:label     "Edit"
                                    :icon      "edit"
                                    :id        (str "edit-" id)
                                    :on-action (fn [_] (swap-fn))}
                                   {:label     "Dispose"
                                    :icon      "close"
                                    :id        (str "delete-" id)
                                    :on-action (fn [_] (dispose-fn id))}]}]])

;context: {:request-ctx {} :data-ctx {}}
(defn property-click-handler
  [property-name property-value {:keys [entity-id] :as ctx}]
  (when (and (= entity-id "continent") (= property-name :id))
    (prn "Load countries for country" property-name " " property-value)
    (rf/dispatch [:entity-requested "countries" {:loader-id  :country-by-id
                                                 :entity-id  "countries"
                                                 "continent" property-value}])))

(defn data-view [context data]
  (prn context)
  [:div {:style {:margin 4}}
   [tree-w/tree-view data {:on-value-click #(property-click-handler %1 %2 context)}]])

(defn config-view [context entity-loader all-loaders on-context-change]
  (let [loader          (get all-loaders entity-loader)
        default-context (entity-loader/default-loader-context loader)]
    [:div
     {:style {:margin 4}}
     [params-panel (:params loader) (merge default-context context {:entity-loader entity-loader}) on-context-change]]))

(defn entity-view [{:keys [id data context entity-loader on-param-change on-entity-dispose] :as entity-data} all-loaders]
  ;TODO: understand why i need to status here?
  (let [state   (r/atom {:status (:status context)})
        swap-fn #(swap! state assoc :status :draft)]
    (fn []
      (prn "Entity Loader " all-loaders)
      [:div.mdc-card.demo-basic-with-header
       [:div
        [entity-title id
         entity-loader
         (assoc context
           :dispose-fn on-entity-dispose
           :swap-fn swap-fn)]]
       (if (= :final (:status @state))
         [data-view context data]
         [config-view context entity-loader all-loaders #(-> (swap! state merge (assoc % :status :final))
                                                             (merge {:status :loading
                                                                     :id     id})
                                                             (on-param-change))])])))
