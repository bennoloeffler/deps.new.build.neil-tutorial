(ns bel.e06.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Views

(defn home-page []
  [:div 
   [:h2 "Welcome to Reagent from core.cljs!"]
   [:div [:p "This is a simple Reagent app."]]
   [:button 
    {:on-click #(js/alert "You clicked me!")}
    "DON'T click me!"]])

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
