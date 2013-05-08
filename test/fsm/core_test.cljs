(ns fsm.core_test
  (:use [fsm.machines :only [machine]])
  (:require [fsm.core :as fsm]))


(defn machine-test []
  (let [test-atom (atom {})
        m (machine "test")]

    (assert (= #{} (fsm/current m)))

    (fsm/in-edge!  m :active #(swap! test-atom assoc :in  %))
    (fsm/out-edge! m :active #(swap! test-atom assoc :out %))

    (fsm/in! m [:active] true)
    (assert (= {:in true} @test-atom))

    (fsm/out! m [:active] true)
    (assert (= {:in true :out true} @test-atom))

    (fsm/transition! m :update #(swap! test-atom assoc :update %))

    (fsm/trigger! m [:update] true)
    (assert (= {:in true :out true :update true}))))


(defn in?-test []
  (let [m (machine "test")]
    (fsm/in! m [:a])
    (assert (true? (fsm/in? m [:a])))
    (assert (false? (fsm/in? m [:b])))))


(defn set-ex-test []
  (let [m (machine "test")]
    (fsm/in! m [:a :b])
    (assert (= #{:a :b} (fsm/current m)))
    (fsm/out-in! m [:b] [:c])
    (assert (= #{:a :c} (fsm/current m)))))


(defn run []
  (machine-test)
  (in?-test)
  (set-ex-test))
