(ns app-server
  (:require [compojure.core :refer [defroutes GET context]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :refer [response header]]
            [clojure.string :as str]
            [ring.middleware.format :refer [wrap-restful-format]]))

(def entity-loaders [{:return-entity "country"
                      :loader-id     :country-by-continent
                      :params        [{:param-id    "continent"
                                       :mandatory?  true
                                       :title       "Continent Id"
                                       :default-val "europe"}]}
                     {:return-entity "city"
                      :loader-id     :city-details
                      :params        [{:param-id "country"}
                                      {:param-id "city-name"}]}

                     {:loader-id       :all-continents
                      :default-loader? true
                      :return-entity   "continent"}])

(defn all-entities
  []
  [{:entity-id       "continent"
    :entity-name     "continent"
    :field-resolvers [{
                       :loader-id :country-by-continent
                       :params    [{:param-id  "continent"
                                    :path-spec [[] :id]}]}]}
   {:entity-id       "country"
    :field-resolvers [{:loader-id :city-details
                       :params    [{:param-id  "country"
                                    :path-spec [[] :name]}
                                   {:param-id  "city-name"
                                    :path-spec [[] :capital]}]}]
    :entity-name     "country"}])


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

(def countries {:asia          [{:name    "China"
                                 :capital "Beijing"}]
                :europe        [{:name    "Romania"
                                 :capital "Bucharest"}
                                {:name    "Moldova"
                                 :capital "Chisinau"}]
                :australia     [{:name    "Australia"
                                 :capital "Canberra"}]
                :africa        [{:name    "Libya"
                                 :capital "Tripoli"}]
                :north-america [{:name    "Canada"
                                 :capital "Ottawa"}
                                {:name    "United States"
                                 :capital "Washington D.C."}]
                :south-america [{:name    "Argentina"
                                 :capital "Buenos Aires"}
                                {:name    "Brazil"
                                 :capital "Brasilia"}]
                :antarctica    ["none"]})


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
                   (response (get countries (keyword (str/lower-case continent)))))
                 (GET "/continents" []
                   (response continents))))


             (wrap-defaults api-defaults)
             wrap-restful-format))
