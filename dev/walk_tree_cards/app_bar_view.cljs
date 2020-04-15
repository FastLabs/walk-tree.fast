(ns walk-tree-cards.app-bar-view
  (:require-macros [devcards.core :refer [defcard-rg defcard]])
  (:require [component.app-bar :as bar]
            [reagent.core :as r]
            [devcards.core]))

(defcard-rg
  content-bar
  (let [state (r/atom "N/A")]
    (fn []
      [:div
       [bar/content-bar {:actions [{:label     "Download"
                                    :icon      "file_download"
                                    :id        "download"
                                    :on-action #(reset! state %)}
                                   {:label     "Print the content"
                                    :id        "print"
                                    :icon      "print"
                                    :on-action #(reset! state %)}
                                   {:label     "Bookmark the content"
                                    :id        "bookmark"
                                    :icon      "bookmark"
                                    :on-action #(reset! state %)}]
                         :title   "Cities"}]
       [:div (str "Content bar: " @state)]])))