(ns bel.e05.views
    (:require
     [re-frame.core :as re-frame]
     [bel.e05.subs :as subs]
     ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]
     ]))
