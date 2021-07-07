(ns entities.events
  (:require [re-frame.core :as rf]
            [martian.re-frame :as mr]
            [entities.loader :as entity-loader]))

(rf/reg-event-db
  :entities-loaded
  (fn [db [_ {:keys [body]} operation-id params]]
    (assoc db :entities {:status    :loaded
                         :available body})))
(rf/reg-event-db
  :loaders-received
  (fn [db [_ {:keys [body]}]]
    (assoc db :loaders body)))

(rf/reg-event-db
  :failed-martian-request
  (fn [db [_ response-or-error operation-id params]]
    (prn "Error to load entity: " response-or-error operation-id params)
    db))

(rf/reg-event-fx
  :entities-requested
  (fn [{:keys [db]} _]
    {:db       db
     :dispatch [::mr/request :entity-specs {} :entities-loaded :failed-martian-request]}))

(rf/reg-event-fx
  :loaders-requested
  (fn [{:keys [db]} _]
    {:db       db
     :dispatch [::mr/request :entity-loaders {} :loaders-received :failed-martian-request]}))



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
  (fn [db [_ {:keys [body]} entity-loader {:keys [entity-id] :as param-ctx}]]
    (prn "entity loaded: " entity-loader entity-id param-ctx)
    (let [entity-id (name entity-id)]
      (update-in db [:loaded entity-id] new-instance {:entity-id     entity-id
                                                      :entity-loader entity-loader
                                                      :context       (assoc param-ctx :status :final)
                                                      :data          body}))))

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
  (fn [{:keys [db]} [_ loader-id load-context]]
    (let [valid-context (entity-loader/validate-loader-context (:loaders db) loader-id load-context)]
      {:db       db
       :dispatch [::mr/request loader-id valid-context :entity-loaded :entity-load-failure]})))

