(ns component.utils)

(defn toggle-class [attrs predicate new-class]
  (let [toggle? (if (fn? predicate) (predicate) predicate)]
    (if toggle?
      (update attrs :class-name #(str % " " new-class))
      attrs)))

(defn toggle-attribute [attrs predicate attribute value]
  (if (predicate)
    (assoc attrs attribute value)
    (dissoc attrs attribute)))