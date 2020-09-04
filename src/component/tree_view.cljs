(ns component.tree-view
  (:require [reagent.core :as ra]
            [clojure.string :as str :refer [lower-case]]))

(defn guess-type [v]
  (cond
    (or (sequential? v) (set? v)) :seq
    (map? v) :map
    :else :other))

(def expandable-types #{:seq :map})

(defn structure-container [subs]
  [:ul subs])

(defn expandable-struct [expanded? on-expand-toggle-fn [struct-open struct-close] struct]
  [:<>
   [:span.struct {:class (when (not expanded?) "collapsed")}
    [:span struct-open]
    (if expanded?
      struct
      [:span {:on-click #(on-expand-toggle-fn (not expanded?))} "..."])
    [:span struct-close]]])

(defn fast-match-fn
  ([v] v)
  ([m v]
   (if (str/blank? m)
     v
     (let [match-str (.toLowerCase m)
           sv (str v)]
       (loop [val-lower (.toLowerCase sv) val-original sv struct [:<>]]
         (let [index (.indexOf val-lower match-str)]
          (if (< index 0)
            (if (>  (count val-original) 0)
              (conj struct [:span val-original])
              struct)
            (let [match-length ( + index (count match-str))
                  prefix (subs val-original 0 index)
                  matched (subs val-original index match-length)]
              (recur (subs val-lower match-length) ;TODO: check if i should not start from index?
                     (subs val-original match-length)
                     (conj struct [:span prefix] [:span {:class "matched-part"} matched]))))))))))


(defn enrich-opts [{:keys [match-val] :as opts}]
  (let [match-fn (partial fast-match-fn match-val)]
    (when match-val
      (assoc opts :name-render-fn (comp match-fn name)
                  :val-render-fn match-fn))))

(defn expander [expanded? show-icon? expand-toggle-handler]
  [:div.collapser {:on-click #(expand-toggle-handler (not expanded?))}
   [:span.property.expandable
    [:i.material-icons {:class [(when expanded? "expanded")
                                (when (not show-icon?) "hidden")]}
     "chevron_right"]]])

(defmulti structure-view (fn [value _] (guess-type value)) :default :string)

(defn property-view [property value {:keys [name-render-fn] :as opts}]
  (let [expanded?           (ra/atom true)
        on-expand-toggle-fn #(swap! expanded? not)
        show-collapse-icon? (contains? expandable-types (guess-type value))]
    (fn []
      (let [exp       @expanded?
            prop-opts (assoc opts
                        :expanded? exp
                        :on-expand-toggle-fn on-expand-toggle-fn)]
        [:div
         [:span (if name-render-fn (name-render-fn property) (str property)) " : "]
         [expander exp show-collapse-icon? on-expand-toggle-fn]
         [structure-view value prop-opts]]))))

(defmethod structure-view :seq [value {:keys [match-val expanded? on-expand-toggle-fn] :as opts}]
  [expandable-struct expanded? on-expand-toggle-fn ["[" "]"]
   [structure-container
    (doall
      (->> value
           (map-indexed (fn [index v]
                          ^{:key (str v "->" match-val "->" index)}
                          [:li                              ;TODO: embed here the expander, but the idea needs to be tested as it could polute the view
                           [structure-view v (enrich-opts opts)]]))))]])

(defmethod structure-view :map [value {:keys [match-val expanded? on-expand-toggle-fn] :as opts}]
  [expandable-struct expanded? on-expand-toggle-fn ["{" "}"]
   [structure-container
    (doall
      (->> value
           (map-indexed
             (fn [index [p v]]
               ^{:key (str p "->" index "->" match-val "->" v)}
               [:li
                [property-view p v (enrich-opts opts)]]))))]])

(defmethod structure-view :string [value {:keys [val-render-fn]}]
  [:span {:class (guess-type value)}
   (if val-render-fn
     (val-render-fn value)
     (str value))])

(defn tree-view
  ([tree-data]
   (tree-view tree-data {}))
  ([tree-data opts]
   (let [opts (merge {:expanded? true} opts)]
     [:div.tree-view
      [structure-view tree-data opts]])))

;other stuff

(defn text-field [_ & [{:keys [placeholder on-change id]
                        :or   {placeholder "search"
                               id          "search-text"}}]]
  (let [focused? (ra/atom false)]
    (fn [txt _]
      [:div.mdl-textfield.in-form {:class (str "optional-input" (when @focused? " focused"))}
       [:div.group-text
        [:i.material-icons {:on-click #(on-change "")} "search"]]
       [:div
        [:input.mdl-textfield__input {:value        txt
                                      :autoComplete :off
                                      :type         :text
                                      :on-focus     #(reset! focused? true)
                                      :on-blur      #(reset! focused? false)
                                      :on-change    #(-> % .-target .-value on-change)
                                      :on-key-up    #(when (= 27 (.-keyCode %)) (on-change ""))
                                      :placeholder  placeholder
                                      :id           id}]]])))

(defmulti filter-struct (fn [value _] (guess-type value)) :default :string)

(defn prop [p v {:keys [match-fn] :as opts}]
  (let [super-match (match-fn p)
        sub-struct  (filter-struct v (assoc opts :match-fn #(or super-match (match-fn %))))]
    (when (not (empty? sub-struct)) [p sub-struct])))

(defmethod filter-struct :map [obj opts]
  (->> obj
       (map (fn [[p v]] (prop p v opts)))
       (filter  #(not (empty? %)))
       (into {})))

(defmethod filter-struct :seq [vals opts]
  (->> vals
       (mapv #(filter-struct % opts))
       (filter #(not (empty? %)))
       (vec)))

(defmethod filter-struct :string [val {:keys [match-fn]}]
  (let [str-val (str val)]
    (when (match-fn str-val) str-val)))

(defn- partial-match [match-str val]
  (str/index-of (lower-case (str val))
                (lower-case match-str)))

(defn searchable-tree []
  (let [search-state (ra/atom "")]
    (fn [data]
      (let [match-str @search-state
            tree-data (filter-struct data {:match-fn (partial partial-match match-str)})]
        [:<>
         [:div {:style {:display :flex :justify-content :fex-end}}
          [text-field match-str
           {:on-change #(reset! search-state %)}]]
         [tree-view tree-data
          {:match-val match-str}]]))))