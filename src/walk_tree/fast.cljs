(ns ^:figwheel-hooks walk-tree.fast
  (:require
    [goog.dom :as gdom]
    [martian.re-frame :as mc]
    [entities.martians :as em]
    [entities.events]
    [component.tree-view :as tree-w]
    [component.search-bar :as search]
    [re-frame.core :as rf]
    [component.text-field :refer [outlined-text-field]]
    [reagent.core :as reagent :refer [atom]]))

(rf/reg-event-db
  ::initialise
  (fn [_ _]
    {:entities {}}))

(rf/reg-fx
  ::entity-request []
  {:db {}})


(rf/reg-sub
  ::entities
  (fn [db _]
    (get-in db [:entities :available])))

(rf/reg-sub
  ::loaded-entities
  (fn [db _]
    (:loaded db)))

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

(defn app-header []
  [:div {:style {:width "100%"}}
   [search/search-bar]])

(defn container []
  (let [entities @(rf/subscribe [::entities])
        loaded @(rf/subscribe [::loaded-entities])]
    [:<>
     [:div
      [app-header]]
     [:div.mdc-layout-grid
      [:div.mdc-layout-grid__cell--span-8
       [:div "Entities: "
        (for [{:keys [entity-name entity-loader] :as entity} entities]
          ^{:key entity} [:button.mdc-button
                          {:on-click #(do
                                        (rf/dispatch [:entity-requested (keyword (:loader-id entity-loader)) {}])
                                        (.preventDefault %))}
                          entity-name])]]
      (prn "--" loaded)
      [:div.mdc-layout-grid__cell--span-8
         (for [{:keys [entity-name] :as entity-data} loaded]
           ^{:key entity-name} [entity-view entity-data])]]]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (reagent/render-component [container] el))


(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;Called from index.html
(defn ^:export init []
  (rf/dispatch-sync [::initialise])
  (rf/dispatch-sync [::mc/init em/entities-martian])
  (rf/dispatch-sync [:entities-requested])
  (mount-app-element))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (init))
