(ns test
  (:require [fsm.core_test :as fsm-test]))


(def success 0)


(defn ^:export run []
  (fsm-test/run)
  success)
