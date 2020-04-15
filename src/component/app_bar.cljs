(ns component.app-bar)

(defn nav-btn [actions]
  [:button.mdc-icon-button.material-icons.mdc-top-app-bar__navigation-icon "menu"])

(defn action-bar [actions]
  [:section.mdc-top-app-bar__section.mdc-top-app-bar__section--align-end
   (for [{:keys [label id icon on-action]} actions]
     ^{:key id}
     [:button.mdc-icon-button.material-icons.mdc-top-app-bar__action-item
      {:aria-label label
       :on-click   (fn [_] (on-action id))} icon])])


(defn content-bar [{:keys [title nav-actions actions]}]
  [:div.mdc-top-app-bar.mdc-top-app-bar--dense {:style {:position                :relative
                                                        :border-top-left-radius  4
                                                        :border-top-right-radius 4}}
   [:div.mdc-top-app-bar__row
    [:section.mdc-top-app-bar__section.mdc-top-app-bar__section--align-start
     (when nav-actions
       [nav-btn nav-actions])
     [:span.mdc-top-app-bar__title title]]
    [action-bar actions]]])
