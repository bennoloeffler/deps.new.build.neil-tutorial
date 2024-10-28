(ns bel.e06.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[e06 starting]=-"))
   :start      (fn []
                 (log/info "\n-=[e06 started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[e06 has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
