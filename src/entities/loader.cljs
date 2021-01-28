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

(defn validate-loader-context [{:keys [entity-id] :as entity} {:keys [loader-id] :as load-context} all-loaders]
  (if loader-id
    (assoc load-context :entity-id entity-id)
    (let [loader-id     (default-loader entity)
          entity-loader (get all-loaders loader-id)]
      (assoc (default-loader-context entity-loader) :loader-id loader-id :entity-id entity-id))))
