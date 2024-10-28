(ns main
  (:require [taoensso.timbre :as timbre
             :refer [trace debug info warn error fatal report]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (info "something")
  (println "Hello, World!"))

(comment
  (-main))