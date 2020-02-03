(ns component.search-bar)


(defn drop-down []
  [:table {:cellpadding 0
           :sellspacing 0
           :style       {:width      "100%"
                         :position   :relative
                         :text-align :left}}
   [:tbody
    [:tr
     [:td]
     [:td {:style {:width "100%"}}
      [:table
       [:tbody {:style {:width "100%"}}]]]]]])


(defn icon-button
  ([icon-name]
   [icon-button {} icon-name])

  ([opts icon-name]
   [:button.mdc-icon-button.search-bar-btn
    opts
    [:i.material-icons.mdc-icon.button__icon icon-name]]))


(defn search-bar []
  [:div.search-bar-container {:margin 10}
   [:form.search-form
    [:div.search-bar
     [:table {:cellSpacing 0
              :cellPadding 0
              :style       {:height 28
                            :width  "100%"}}
      [:tbody>tr>td
       [:table {:cellSpacing 0
                :cellPadding 0
                :style       {:width "100%" :height 28}}
        [:tbody
         [:tr
          [:td.search-cell {:style {:padding-top    11
                                    :height         :inherit
                                    :padding-bottom 11}}
           [:div {:style {:position :relative}}
            [:input.text-box {:type         :text           ;;TODO: check the aria attributes it makes any sence
                              :aria-label   "Search Entities"
                              :autoComplete :off
                              :placeholder  "Search Entities"
                              :spellCheck   false
                              :style        {:border   :none
                                             :padding  0
                                             :margin   0
                                             :height   :auto
                                             :width    "100%"
                                             :position :absolute
                                             :z-index  6
                                             :left     0}}]
            [:input.text-box {:disabled       true
                              :autoCapitalize "off"
                              :autoComplete   "off"
                              :style          {:border                  :none
                                               :padding                 0
                                               :margin                  0
                                               :height                  :auto
                                               :width                   "100%"
                                               :position                :absolute
                                               :z-index                 1
                                               :background-color        "transparent"
                                               :-webkit-text-fill-color "silver"
                                               :color                   "silver"
                                               :left                    0
                                               :visibility              "hidden"}}]]]]]]]]]

    [icon-button {:style {:position :absolute :right 0 :top 0}} "arrow_drop_down"]
    [icon-button {:style {:position :absolute :top 0 :right 37}} "close"]
    [icon-button {:style {:position :absolute :float :left :top 0}} "search"]]])

