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

(rf/reg-sub
  :loaded-entities
  (fn [db _]
    (->> (vals (:loaded db))
         (map :instances)
         ;(map vals)
         (apply concat))))

(defn app-header []
  [:div {:style {:width "100%"}}
   [search/search-bar]])

(rf/reg-event-fx
  :update-instance
  (fn [{:keys [db]} [_ entity-name context]]
    (prn entity-name context)
    (let [db' (update-in db
                         [:loaded entity-name :instances (:id context)]
                         (fn [current]

                           (-> (dissoc current :data)
                               (assoc :context context))))]
      (prn (:loaded db'))
      {:db       db'
       :dispatch [:entity-requested entity-name context]})))


(defn container []
  (let [entities @(rf/subscribe [:entities])
        loaded   @(rf/subscribe [:loaded-entities])]
    [:<>
     [:div
      [app-header]]
     [:div.mdc-layout-grid
      [:div.mdc-layout-grid__cell--span-8
       [:div "Entities: "
        (for [{:keys [entity-name entity-id] :as entity} entities]
          ^{:key entity} [:button.mdc-button
                          {:on-click #(do
                                        (rf/dispatch [:entity-requested entity-id])
                                        (.preventDefault %))}
                          entity-name])]]
      (prn "--" loaded)
      [:div.mdc-layout-grid__cell--span-8
       (for [[id {:keys [context entity-name] :as entity-data}] loaded]
         ^{:key {:id          id
                 :status      (:status context)
                 :entity-name entity-name}}
         [entity-view (assoc entity-data :on-param-change
                                         (fn [new-context]
                                           (let [context (merge new-context {:id     id
                                                                             :status :loading})]
                                             (rf/dispatch [:update-instance (:entity-name entity-data) context]))))])]]]))

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
  (rf/dispatch-sync [:entities-requested])
  (mount-app-element))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (init))
