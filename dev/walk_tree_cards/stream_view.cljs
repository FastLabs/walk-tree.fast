(ns walk-tree-cards.stream-view
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [devcards.core]
            [reagent.core :as ra]))

(defn action-btn [title & {:keys [icon on-click]}]
  [:button.mdc-icon-button.mdc-card__action.mdc-card__action--icon--unbounded.my-button
   {;:aria-pressed "false"
    :aria-label title
    :on-click   #(when on-click (on-click))
    :title      title}
   (when icon
     [:i.material-icons.mdc-icon-button__icon
      icon])])

(defn stream-control
  [{:keys [id status]}]
  [:div.mdc-card.mdc-card-outlined {:style {:width 260}}
   [:div.mdc-card__primary-action
    {:style {:padding 3}}
    [:div {:style {:display     :flex :flex-direction :row
                   :margin      4
                   :align-items :center
                   :box-sizing  :border-box}}

     [:div.mdc-typography.mdc-typography--headline6 {:style {:width "100%"}} id]
     [:div {:style {:display         :flex
                    :width           90
                    :justify-content :flex-end
                    :flex-row        1}}
      [:i.material-icons {:style {:color :green :padding 3}}
       "play_circle_outline"]]]]

   [:div.mdc-card-actions {:style {:margin 4}}
    [:div.mdc-card__action-icons
     (cond
       (= status :dead) [action-btn "Start" :icon "play_arrow" :on-click #(prn "Start")]
       (= status :live) [action-btn "Stop" :icon "stop" :on-click #(prn "Stop")]
       :else [:span])]]])

(defn stream-panel [streams]
  (for [{:keys [id] :as s} streams]
    ^{:key (str "-" id)}
    [stream-control s]))

(defn stream-by-status
  ([streams]
   (stream-by-status {} streams))
  ([{:keys [id]} streams]
   (for [[group-name group-streams] (group-by :status streams)]
     (do
       (prn group-streams)
       ^{:key (str id "-" group-name)}
       [:div group-name]
       [:div]
       [stream-panel group-streams]))))

(def demo-streams [{:id         "Ioana"
                    :status     :live
                    :sub-status :snapshot-complete}
                   {:id     "stream 2"
                    :status :dead}
                   {:id     "stream 3"
                    :status :pending}])

(defcard-rg
  stream-action
  (let [[stream-live
         stream-dead
         stream-pending] demo-streams]
    [:div
     [:div {:style {:margin 5}}
      [stream-control stream-live]]
     [:div {:style {:margin 5}}
      [stream-control stream-dead]]
     [:div {:style {:margin 5}}
      [stream-control stream-pending]]]))

(defcard-rg
  streams-by-status
  (stream-by-status demo-streams))

