(ns fsm.core
  (:refer-clojure :exclude [set])
  (:require [fsm.protocols :as p]))


(defn current [this]
  (p/-current this))


(defn in!
  ([this states]
     (in! this states nil))
  ([this states context]
     (p/-in! this states context)))


(defn out!
  ([this states]
     (out! this states nil))
  ([this states context]
     (p/-out! this states context)))


(defn trigger!
  ([this transitions]
     (trigger! this transitions nil))
  ([this transitions context]
     (p/-trigger! this transitions context)))


(defn in-edge! [this state f]
  (p/-in-edge! this state f))


(defn out-edge! [this state f]
  (p/-out-edge! this state f))


(defn transition! [this transition f]
  (p/-transition! this transition f))


(defn in? [machine states]
  (every? (current machine) states))


(defn out-in!
  ([machine to-unset to-set]
     (out-in! machine to-unset to-set nil))
  ([machine to-unset to-set context]
     (out! machine to-unset context)
     (in! machine to-set context)))
