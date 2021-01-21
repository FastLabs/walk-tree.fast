(ns ^:figwheel-hooks walk-tree.fast
  (:require
    [goog.dom :as gdom]
    [martian.re-frame :as mc]
    [entities.martians :as em]
    [entities.events]

    [component.search-bar :as search]
    [entities.entity-views :refer [entity-view]]
    [re-frame.core :as rf]
    [component.text-field :refer [outlined-text-field]]
    [reagent.core :refer [atom]]
    [reagent.dom :as r-dom]))

(rf/reg-event-db
  :initialise
  (fn [_ _]
    {:entities {}
     :loaded   {}}))

(rf/reg-fx
  :entity-request []
  {:db {}})


(rf/reg-sub
  :entities
  (fn [db _]
    (get-in db [:entities :available])))

(defn lookup-entity-spec [available-entities entity-id]
  (first (filter #(= ( :entity-id % ) entity-id) available-entities)))

(rf/reg-sub
  :loaded-entities
  (fn [{:keys [:entities] :as db} _]
    (->> (vals (:loaded db))
         (map :instances)
         ;(map vals)
         (apply concat))))

(rf/reg-sub
  :all-loaders
  (fn [db _]
    (:loaders db)))

(defn app-header []
  [:div {:style {:width "100%"}}
   [search/search-bar]])

(rf/reg-event-fx
  :update-instance
  (fn [{:keys [db]} [_ entity-name context]]
    (let [db' (update-in db
                         [:loaded entity-name :instances (:id context)]
                         (fn [current]
                           (-> (dissoc current :data)
                               (assoc :context context))))]
      (prn "update entity: " entity-name context)
      {:db       db'
       :dispatch [:entity-requested entity-name context]})))

(defn loaded-entities [available-entities loaded]
  (let [all-loaders @(rf/subscribe [:all-loaders])]
    [:div.mdc-layout-grid__cell--span-8
     (for [[id {:keys [context entity-id entity-loader] :as entity-data}] loaded]
       ^{:key {:id          id
               :status      (:status context)
               :entity-id entity-id}}
       [entity-view
        (lookup-entity-spec available-entities entity-id)
        (merge entity-data
               {:id                id
                :entity-loader (assoc  (get all-loaders entity-loader) :loader-id entity-loader)
                :on-entity-dispose (fn [dispose-id]
                                     (rf/dispatch [:dispose-instance entity-id dispose-id]))
                :on-param-change   (fn [new-context]
                                     (rf/dispatch [:update-instance entity-id new-context]))})])]))


(defn container []
  (let [entities @(rf/subscribe [:entities])                ;;TODO: maybe join the loaders in this subscription to avoid later lookup in :entity-requested event handler
        loaded   @(rf/subscribe [:loaded-entities])]
    [:<>
     #_[:div
        [app-header]]
     [:div.mdc-layout-grid
      [:div.mdc-layout-grid__cell--span-8
       [:div "Entities: "
        (for [{:keys [entity-name entity-id] :as entity} entities]
          ^{:key entity} [:button.mdc-button
                          {:on-click #(do
                                        (rf/dispatch [:entity-requested entity-id {}])
                                        (.preventDefault %))}
                          entity-name])]]
      [loaded-entities entities loaded]]]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (r-dom/render [container] el))


(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;Called from index.html
(defn ^:export init []
  (rf/dispatch-sync [:initialise])
  (rf/dispatch-sync [::mc/init em/entities-martian])
  (rf/dispatch-sync [:loaders-requested])
  (rf/dispatch-sync [:entities-requested])
  (mount-app-element))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (init))
