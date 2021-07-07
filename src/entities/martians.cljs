(ns entities.martians
  (:require [martian.re-frame]
            [schema.core :as s]
            [martian.core :as martian]
            [martian.cljs-http :as martian-http]))


(def entities-martian (martian-http/bootstrap
                        "/api/v1"
                        [{:route-name :entity-specs
                          :path-parts "/entity/specs"
                          :method     :get}

                         {:route-name :entity-loaders
                          :path-parts "/entity/loaders"
                          :method     :get}

                         {:route-name :all-continents
                          :path-parts "/geo/continents"
                          :method     :get}

                         {:route-name  :country-by-continent
                          :path-parts  ["/geo/countries/" :continent]
                          :method      :get
                          :path-schema {:continent s/Str}}]))


(defn get-path [route-name ctx]
  (martian/url-for entities-martian route-name ctx))