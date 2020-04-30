(ns app-server
  (:require [compojure.core :refer [defroutes GET context]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :refer [response header]]
            [clojure.string :as str]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defn all-entities
  []
  [{:entity-id     "continent"
    :entity-name   "continent"
    :entity-loader {:loader-id :continents}}
   {:entity-id     "countries"
    :entity-name   "countries"
    :entity-loader {:loader-id :countries
                    :params    [{:param-id    "continent"
                                 :mandatory?   true
                                 :title  "continent id"
                                 :default-val "europe"}]}}
   {:entity-id "entity-3"}])

(def continents [
                 {:name "Africa"
                  :id   :africa}
                 {:name "North America"
                  :id   :north-america}
                 {:name "South America"
                  :id   :south-america}
                 {:name "Europe"
                  :id   :europe}
                 {:name "Asia"
                  :id   :asia}
                 {:name "Antarctica"
                  :id   :antarctica}
                 {:name "Australia"
                  :id   :australia}])

(def countries {:asia   [{:name    "China"
                          :capital "xxx"}]
                :europe [{:name    "Romania"
                          :capital "Bucharest"}]})


(defroutes handler
           (->
             (context "/api/v1" []
               (GET "/entity/specs" []
                 (-> (response (all-entities))
                     #_(header "Content-Type" "application/json")))
               #_(GET "/app/:app-id" [app-id]
                   (response {:app-id (str "app->" app-id)}))
               (context "/geo" []
                 (GET "/countries/:continent" [continent]
                   (response (get countries (keyword (str/lower-case continent)))))
                 (GET "/continents" []
                   (response continents))))


             (wrap-defaults api-defaults)
             wrap-restful-format))
