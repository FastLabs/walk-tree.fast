(ns component.text-field
  (:require [clojure.string :as str]
            [reagent.core :as ra]))

(defn toggle-class [attrs predicate new-class]
  (if (predicate)
    (update attrs :class-name #(str % " " new-class))
    attrs))

(defn toggle-attribute [attrs predicate attribute value]

  (if (predicate)
    (assoc attrs attribute value)
    (dissoc attrs attribute)))

(defn text-field-helper
  ([hep-text]
   [text-field-helper {} hep-text])
  ([{:keys [id] :as attrs} help-text]
   [:div.mdc-text-field-helper-line
    [:p.mdc-text-field-helper-text.mdc-text-field-helper-text--persistent.mdc-text-field-helper-text--validation-msg
     (merge attrs {:id (str id "-helper-text")}) help-text]]))


(defmulti text-field-label (fn [type _] type) :default :standard)

(defmethod text-field-label :standard [_ {:keys [for-el placeholder raised?]}]
  [:<>
   [:label (-> {:for        for-el
                :class-name "mdc-floating-label"}
               (toggle-class #(= raised? true) "mdc-floating-label--float-above")) placeholder]
   [:div.mdc-line-ripple {:style {:transform-origin "195px center"}}]])

(defmethod text-field-label :outlined [_ {:keys [raised? placeholder for-el]}]
  [:div#notch-outline
   (-> {:class-name "mdc-notched-outline mdc-notched-outline--upgraded"}
       (toggle-class #(= raised? true) "mdc-notched-outline--notched"))
   [:div.mdc-notched-outline__leading]
   [:div.mdc-notched-outline__notch {:style (toggle-attribute {} #(= raised? true) :width 56.75)}
    [:label
     (-> {:for        for-el
          :class-name "mdc-floating-label"}
         (toggle-class #(= raised? true) "mdc-floating-label--float-above")) placeholder]]
   [:div.mdc-notched-outline__trailing]])


(defn outlined-text-field [{:keys [id icon placeholder help-text type value]
                            :or   {placeholder "text"
                                   value       ""
                                   type        :standard}}]
  (let [status (ra/atom :off)
        label-opts {:for-el id :placeholder placeholder}
        text-value (ra/atom value)
        on-cancel #(prn "Cancel requested")
        on-submit #(prn "Submit requested")
        on-new-value #(reset! text-value %)
        text-outer-attrs (-> {:class-name "mdc-text-field text-field my-edit-1"}
                             (toggle-class #(= type :outlined) "mdc-text-field--outlined")
                             (toggle-class #(not (nil? icon)) "mdc-text-field--with-leading-icon "))]
    (fn []

      (let [raised (or (= :on @status) (> (count @text-value) 0))]
        [:div.text-field-container
         [:div#text-outer
          (-> text-outer-attrs
              (toggle-class #(= raised true) "mdc-text-field--focused"))
          (when icon [:i.material-icons.mdc-text-field__icon icon])
          [:input.mdc-text-field__input
           {:type      "text"
            :value     @text-value
            :on-blur   #(reset! status :off)
            :on-focus  #(reset! status :on)
            :on-key-up #(case (.-keyCode %)
                              27 (on-cancel)
                              13 (on-submit))
            :on-change #(on-new-value (-> % .-target .-value))
            :id        id}]
          [text-field-label type (assoc label-opts :raised? raised)]]
         (when help-text
           [text-field-helper {:id "text-field-outlined-leading"} help-text])]))))
