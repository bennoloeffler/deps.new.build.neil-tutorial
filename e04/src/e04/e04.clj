(ns e04.e04
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, brave new " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))
