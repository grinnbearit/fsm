(ns fsm.protocols)


(defprotocol IMachine
  (-current [this])
  (-in! [this states context])
  (-out! [this states context])
  (-trigger! [this transitions context])
  (-in-edge! [this state f])
  (-out-edge! [this state f])
  (-transition! [this state f]))
