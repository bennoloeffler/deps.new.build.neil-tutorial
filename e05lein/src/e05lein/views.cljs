(ns e05lein.views
  (:require
   [re-frame.core :as re-frame]
   [e05lein.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]))
