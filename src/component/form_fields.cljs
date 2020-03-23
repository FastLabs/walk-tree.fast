(ns component.form-fields)



(defn form-field [form-control label]
  [:div.mdc-form-field
   form-control
   [:label label]])

(defn check-box [{:keys [id]}]
  [:div.mdc-checkbox
   [:input.mdc-checkbox__native-control
    {:type :checkbox
     :id id}]
   [:div.mdc-checkbox__background]])




