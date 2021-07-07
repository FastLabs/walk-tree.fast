(ns entities.loader)

(defn default-loader-context [entity-loader]
  (->> (:params entity-loader)
       (filter :default-val)
       (map (fn [{:keys [param-id default-val]}] [param-id default-val]))
       (into {})))

(defn default-loader [{:keys [entity-loaders]}]
  (-> (first entity-loaders)
      :loader-id
      (keyword)))

(defn lookup-loader [available-loaders id]
  (prn available-loaders)
  (first  (filter (fn [{:keys [loader-id]}] (= id (keyword loader-id))) available-loaders)))


(defn validate-loader-context [ all-loaders loader-id load-context]
  (let [{:keys [return-entity] :as loader} (lookup-loader all-loaders loader-id)]
    (merge (default-loader-context loader) {:entity-id return-entity} load-context)))

