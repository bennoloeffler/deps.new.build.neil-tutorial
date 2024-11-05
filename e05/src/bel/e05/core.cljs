(ns bel.e05.core
    (:require
     [reagent.dom :as reagent]
     [re-frame.core :as re-frame]
     [bel.e05.events :as events]
     [bel.e05.views :as views]
     [bel.e05.config :as config]
     ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
