(ns bel.e06.env
  (:require
    [clojure.tools.logging :as log]
    [bel.e06.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[e06 starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[e06 started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[e06 has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
