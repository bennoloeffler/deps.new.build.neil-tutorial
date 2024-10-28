(ns deps-tutorial.main
  (:gen-class))

(defn -main [& opts]
  (let [h "Hello, world!"]
    (prn h  opts)
    h))