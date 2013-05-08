(ns fsm.machines
  (:require [fsm.protocols :as p]))


(deftype Machine [data])


(extend-type Machine
  p/IMachine
  (-current [this]
    (get-in @(.-data this) [:current]))

  (-in! [this states context]
    (doseq [state states]
      (when (not ((get-in @(.-data this) [:current]) state))
        (swap! (.-data this) update-in [:current] conj state)
        (when-let [in (get-in @(.-data this) [:states state :in])]
          (in context)))))

  (-out! [this states context]
    (doseq [state states]
      (when ((get-in @(.-data this) [:current]) state)
        (swap! (.-data this) update-in [:current] disj state)
        (when-let [out (get-in @(.-data this) [:states state :out])]
          (out context)))))

  (-trigger! [this transitions context]
    (doseq [trans transitions]
      (when-let [ts (get-in @(.-data this) [:transitions trans])]
        (ts context))))

  (-in-edge! [this state f]
    (swap! (.-data this) assoc-in [:states state :in] f))

  (-out-edge! [this state f]
    (swap! (.-data this) assoc-in [:states state :out] f))

  (-transition! [this transition f]
    (swap! (.-data this) assoc-in [:transitions transition] f)))


(deftype DebugMachine [id fsm])


(extend-type DebugMachine
  p/IMachine
  (-current [this]
    (p/-current (.-fsm this)))

  (-in! [this states context]
    (let [old (p/-current this)
          result (p/-in! (.-fsm this) states context)]
      (.log js/console (.-id this) "in!" (pr-str context) ":" (pr-str old) "->" (pr-str (p/-current (.-fsm this))))
      result))

  (-out! [this states context]
    (let [old (p/-current this)
          result (p/-out! (.-fsm this) states context)]
      (.log js/console (.-id this) "out!" (pr-str context) ":" (pr-str old) "->" (pr-str (p/-current (.-fsm this))))
      result))

  (-trigger! [this transitions context]
    (.log js/console (.-id this) ">" (pr-str transitions) (pr-str context))
    (p/-trigger! (.-fsm this) transitions context))

  (-in-edge! [this state f]
    (p/-in-edge! (.-fsm this) state f))

  (-out-edge! [this state f]
    (p/-out-edge! (.-fsm this) state f))

  (-transition! [this transition f]
    (p/-transition! (.-fsm this) transition f)))


(defn machine
  ([id]
     (Machine. (atom {:current #{} :states {} :transitions {}})))
  ([id debug?]
     (if debug?
       (DebugMachine. id (machine nil))
       (machine id))))
