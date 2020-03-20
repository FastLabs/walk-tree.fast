(ns component.param-panel
  (:require
    [reagent.core :as r]
    [component.text-field :refer [outlined-text-field]]))
;TODO: should be as multi for edit mode: view or edit

(defmulti param-editor :type :default :text)

(defmethod param-editor :text [{:keys [title value id on-change]}]
  [outlined-text-field {:placeholder title
                        :on-change   (partial on-change id)
                        :value       value}])

(defmulti params-panel (fn [entity] :draft) :default :draft)


(defmethod params-panel :draft [{:keys [params]} context]
  (let [state (r/atom {})
        on-change-fn (fn [property value]
                       (swap! state assoc property value))]
    [:div {:style {:display :flex :margin-bottom 20}}
     (for [{:as param :keys [id]} params]
       ^{:key param} [:div {:style {:margin-right 20}}
                      [param-editor (assoc param :value (get context id)
                                                 :on-change on-change-fn)]])
     [:div
      [:button.mdc-button
       {:on-click #(prn "Request entity1" (merge context @state) )} "GET"]]]))
