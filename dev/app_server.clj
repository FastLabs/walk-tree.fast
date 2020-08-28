(ns app-server
  (:require [compojure.core :refer [defroutes GET context]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :refer [response header]]
            [clojure.string :as str]
            [ring.middleware.format :refer [wrap-restful-format]]))

(def entity-loaders {:country-by-id {:params [{:param-id    "continent"
                                               :mandatory?  true
                                               :title       "Continent Id"
                                               :default-val "europe"}]}
                     :continents    {}})

(defn all-entities
  []
  [{:entity-id       "continent"
    :entity-name     "continent"
    :field-resolvers [{
                       :loader-id :country-by-id
                       :params    [{:param-id "continent"
                                    :path     [:id]}]}]
    :entity-loaders  [{:loader-id :continents}]}
   {:entity-id      "countries"
    :entity-name    "countries"
    :entity-loaders [{:loader-id       :country-by-id
                      :default-loader? true}]}])


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
                          :capital "Bucharest"}
                         {:name    "Moldova"
                          :capital "Chisinau"}]})


(defroutes handler
           (->
             (context "/api/v1" []
               (context "/entity" []
                 (GET "/loaders" []
                   (response entity-loaders))
                 (GET "/specs" []
                   (response (all-entities))))
               #_(GET "/app/:app-id" [app-id]
                   (response {:app-id (str "app->" app-id)}))
               (context "/geo" []
                 (GET "/countries/:continent" [continent]
                   (prn "+++++" continent)
                   (response (get countries (keyword (str/lower-case continent)))))
                 (GET "/continents" []
                   (response continents))))


             (wrap-defaults api-defaults)
             wrap-restful-format))
