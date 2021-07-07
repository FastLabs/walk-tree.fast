(ns walk-tree.fast-test
  (:require [cljs.test :refer-macros [deftest is testing]]))


(defn build-param-context [entity params-spec])


(deftest path-inspection
  (let [structure []
        params [{:param-id "continent"
                 :path-spec [[] :id]}]]
    (testing "simple path 1"
      (is true))
    (testing "another simple test"
      (is true))))