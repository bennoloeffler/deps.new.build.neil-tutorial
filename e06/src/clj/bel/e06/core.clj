(ns bel.e06.core
  (:require
    [clojure.tools.logging :as log]
    [integrant.core :as ig]
    [bel.e06.config :as config]
    [bel.e06.env :refer [defaults]]

    ;; Edges  
    [kit.edge.db.xtdb]
    [kit.edge.db.sql.conman]
    [kit.edge.db.sql.migratus]
    [kit.edge.db.postgres]
    [kit.edge.db.mysql]     
    [kit.edge.server.undertow]
    [bel.e06.web.handler]

    ;; Routes
    [bel.e06.web.routes.api]
    
    [bel.e06.web.routes.pages])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
  (fn [thread ex]
      (log/error {:what :uncaught-exception
                  :exception ex
                  :where (str "Uncaught exception on" (.getName thread))})))

(defonce system (atom nil))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!)))

(defn start-app [& [params]]
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/expand)
       (ig/init)
       (reset! system)))

(defn -main [& _]
  (start-app)
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
  (.addShutdownHook (Runtime/getRuntime) (Thread. shutdown-agents)))