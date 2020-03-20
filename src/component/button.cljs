(ns component.button
  (:require [component.utils :refer [toggle-class]]))

(def button-types {:raised      {:class-name "mdc-button--raised"}
                   :un-elevated {:class-name "mdc-button--unelevated"}
                   :outlined    {:class-name "mdc-button--outlined"}
                   :shaped      {}})

(defn button
  [{:keys [label on-click type style icon density]
    :or   {label "btn"
           style {}}}]
  (let [default-attrs {:class-name "mdc-button"
                       :style      style
                       :on-click   on-click}
        {:keys [class-name]} (get button-types type)]
    [:button (-> (toggle-class default-attrs #(not (nil? class-name)) class-name)
                 (toggle-class #(not= 0 density) (str "dense-btn" density)))
     [:span.mdc-button__ripple]
     (when icon
       [:i.material-icons.mdc-button__icon icon])
     [:span.mdc-button__label label]]))