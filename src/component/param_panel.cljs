(ns component.param-panel
  (:require
    [reagent.core :as r]
    [component.text-field :refer [outlined-text-field]]))
;TODO: should be as multi for edit mode: view or edit

(defmulti param-editor :type :default :text)

(defmethod param-editor :text [{:keys [title value param-id on-change disabled?]}]
  (prn disabled?)
  [outlined-text-field {:placeholder title
                        :density     -4
                        :disabled?   disabled?
                        :on-change   (partial on-change param-id)
                        :value       value}])


(defn params-panel [params context on-context-change]
  (let [state        (r/atom {})
        on-change-fn (fn [property value]
                       (swap! state assoc property value))]
    (fn []
      [:div {:style {:display :flex :margin-bottom 10}}
       (for [{:as param :keys [param-id]} params]
         ^{:key param}
         [:div {:style {:margin-right 10}}
          [param-editor (assoc param :value (get context param-id)
                                     :on-change on-change-fn)]])
       [:div
        [:button.mdc-button
         {:on-click #(on-context-change (merge context @state))} "ok"]]])))
