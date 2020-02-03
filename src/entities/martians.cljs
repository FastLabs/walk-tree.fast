(ns entities.martians
  (:require [martian.re-frame :as mr]
            [martian.cljs-http :as martian-http]))


(def entities-martian (martian-http/bootstrap
                        "/api/v1"
                        [{:route-name :entity-specs
                          :path-parts "/entity/specs"
                          :method     :get}

                         {:route-name :continents
                          :path-parts "/geo/continents"
                          :method     :get}

                         {:route-name :country
                          :path-parts "/geo/country/:country-id"
                          :method     :get}]))

