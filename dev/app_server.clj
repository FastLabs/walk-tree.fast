(ns app-server
  (:require [compojure.core :refer [defroutes GET context]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.util.response :refer [response header]]
            [clojure.string :as str]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defn all-entities
  []
  [{:entity-id       "continent"
    :entity-name     "continent"
    :entity-loader   {:loader-id :continents
                      :params    {:param-name "Name"}}
    :field-resolvers {:field-path ["city"]}}
   {:entity-id "entity-2"}
   {:entity-id "entity-3"}])

(def continents [
                 {:name "Asia"}
                 {:name "North-America"}])

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
