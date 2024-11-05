(ns bel.e05.events
    (:require
     [re-frame.core :as re-frame]
     [bel.e05.db :as db]
     ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
