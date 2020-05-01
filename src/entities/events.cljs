(ns entities.events
  (:require [re-frame.core :as rf]
            [martian.re-frame :as mr]))

(defn entity-spec [{:keys [entities]} entity-id]
  (->> (:available entities)
       (filter #(= (:entity-id %) entity-id))
       (first)))

(defn loader-context [entity-loader]
  (->> (:params entity-loader)
       (filter :default-val)
       (map (fn [{:keys [param-id default-val]}] [param-id default-val]))
       (into {})))

(rf/reg-event-db
  :entities-loaded
  (fn [db [_ {:keys [body]} operation-id params]]
    (assoc db :entities {:status    :loaded
                         :available body})))

(rf/reg-event-db
  :entities-load-failure
  (fn [db [_ response-or-error operation-id params]]
    (prn "Error to load entity" response-or-error operation-id params)
    db))

(rf/reg-event-fx
  :entities-requested
  (fn [{:keys [db]} _]
    {:db       db
     :dispatch [::mr/request :entity-specs {} :entities-loaded :entities-load-failure]}))

(defn new-instance [{:keys [ins-count instances]
                     :or {ins-count 0
                          instances {}} :as container} new-entity]
  (if-let [id (get-in new-entity [:context :id])]
    (merge container
           {:instances (assoc instances id new-entity)})
    (merge container
           {:ins-count (inc ins-count)
            :instances (assoc instances (str "inst-" ins-count) new-entity)})))


;entity load events
(rf/reg-event-db
  :entity-loaded
  (fn [db [_ {:keys [body]} entity-id param-ctx]]
    (let [entity-id (name entity-id)
          {:keys [entity-loader]} (entity-spec db entity-id)
          context   (merge (loader-context entity-loader) param-ctx)]
      (update-in db [:loaded entity-id] new-instance {:entity-name entity-id
                                                      :context     (merge context {:status :final})
                                                      :params      (:params entity-loader)
                                                      :data        body}))))

(rf/reg-event-db
  :dispose-instance
  (fn [db [_ entity-name entity-id]]
    (update-in db [:loaded entity-name :instances] dissoc entity-id)))

(rf/reg-event-db
  :entity-load-failure
  (fn [db err]
    (prn "Error fetching the entity" err)
    db))

(rf/reg-event-fx
  :entity-requested
  (fn [{:keys [db]} [_ entity-id load-context]]
    (let [{:keys [entity-loader]} (entity-spec db entity-id)
          load-context (merge (loader-context entity-loader) load-context)]
      {:db       db
       :dispatch [::mr/request (keyword (:loader-id entity-loader)) load-context :entity-loaded :entity-load-failure]})))

