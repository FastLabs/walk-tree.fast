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


(defn entity-title [id {:keys [loader-id params]} {:keys [swap-fn dispose-fn] :as context}]
  (let [close-action {:label     "Dispose"
                      :icon      "close"
                      :id        (str "delete-" id)
                      :on-action (fn [_] (dispose-fn id))}]
    [:div
     [app-bar/content-bar {:title   (resource-title loader-id context)
                           :actions (if (not (empty? params)) [{:label     "Edit"
                                                                :icon      "edit"
                                                                :id        (str "edit-" id)
                                                                :on-action (fn [_] (swap-fn))} close-action]
                                                              [close-action])}]]))


;context: {:request-ctx {} :data-ctx {}}
(defn property-click-handler
  [property-name property-value {:keys [entity-id] :as ctx}]
  (when (and (= entity-id "continent") (= property-name :id))
    (prn "Load countries for country" property-name " " property-value)
    (rf/dispatch [:entity-requested "countries" {:loader-id  :country-by-id
                                                 :entity-id  "countries"
                                                 "continent" property-value}])))

(defn path-matched? [path-template path]
  (= (keyword (second  path-template)) (second path)))

(defn match-resolver [field-resolvers path]
  (first (filter #(path-matched? (-> field-resolvers first :params first :path) path) field-resolvers)))

;TODO: review context content, maybe loader context is overpopulated maybe better to take loader outside
(defn data-view [ {:keys [field-resolvers] :as x} context data]
  [:div {:style {:margin 4}}
   [tree-w/tree-view data {:on-value-click #(property-click-handler %1 %2 context)
                           :val-render-fn  (fn [path val]
                                             (if-let [resolver  (match-resolver field-resolvers path)] ;TODO: finish the resolver part
                                               [:span {:style {:color "red"}} val]
                                               [:span val]))}]])

(defn config-view [context entity-loader on-context-change]
  (let [default-context (entity-loader/default-loader-context entity-loader)]
    [:div
     {:style {:margin 4}}
     [params-panel (:params entity-loader) (merge default-context context {:entity-loader entity-loader}) on-context-change]]))

(defn entity-view [entity-spec {:keys [id data context entity-loader on-param-change on-entity-dispose] :as entity-data}]
  (let [state   (r/atom {:status (:status context)})
        swap-fn #(swap! state assoc :status :draft)]
    (fn []
      [:div.mdc-card.demo-basic-with-header
       [:div
        [entity-title id
         entity-loader
         (assoc context
           :dispose-fn on-entity-dispose
           :swap-fn swap-fn)]]
       (if (= :final (:status @state))
         [data-view entity-spec context data]
         [config-view context entity-loader #(-> (swap! state merge (assoc % :status :final))
                                                 (merge {:status :loading
                                                         :id     id})
                                                 (on-param-change))])])))
