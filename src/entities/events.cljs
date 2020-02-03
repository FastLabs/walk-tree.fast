(ns entities.events
  (:require [re-frame.core :as rf]
            [martian.re-frame :as mr]))


(rf/reg-event-db
  ::entities-loaded
  (fn [db [_ {:keys [body]} operation-id params]]
    (prn body)
    (assoc db :entities {:status    :loaded
                         :available body})))

(rf/reg-event-db
  ::entities-load-failure
  (fn [db [_ response-or-error operation-id params]]
    (prn "Error to load entity" response-or-error operation-id params)
    db))

(rf/reg-event-fx
  :entities-requested
  (fn [{:keys [db]} _]
    {:db       db
     :dispatch [::mr/request :entity-specs {} ::entities-loaded ::entities-load-failure]}))

;entity load events
(rf/reg-event-db
  ::entity-loaded
  (fn [db [_ {:keys [body]} operation-id params]]
    (update-in db [:loaded] conj {:entity-name (name operation-id)
                                  :data body})))

(rf/reg-event-db
  ::entity-load-failure
  (fn [db _]
    (prn "Error fetching the entity")
    db))

(rf/reg-event-fx
  :entity-requested
  (fn [{:keys [db]} [_ entity-loader loader-params]]
    {:db       db
     :dispatch [::mr/request entity-loader loader-params ::entity-loaded ::entity-load-failure]}))

