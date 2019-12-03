(ns ^:figwheel-hooks walk-tree.fast
  (:require
   [goog.dom :as gdom]
   [component.tree-view :as tree-w]
   [reagent.core :as reagent :refer [atom]]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))


(defn hello-world []
  (let [data {:name    "Oleg Bulavitchi"
              :some    {:some1 {:lala "foc"}}
              :address {:country  "Modlova"
                        :location "Ciobalaccia"}
              :studies [{:name  "Scoala Ciobalaccia" :location "Ciobalaccia"
                         :super {:name "test"}}
                        {:name "Universitatea Tehnica" :location "Chishinau"}]}]
    [:<>
     [:div
      [:h1 (:text @app-state)]
      [:h3 "Simple Tree"]
      [:div.mdc-card {:style {:padding 10}}
       [tree-w/tree-view data]]]]))

(defn get-app-element []
  (gdom/getElement "app"))


(defn mount [el]
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element))
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

