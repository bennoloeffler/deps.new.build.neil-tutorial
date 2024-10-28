(ns deps-tutorial.main
  (:require [java-time.api :as jt]))

(defn -main [opts]
  (let [hello "Hello, world!" 
        today (str (jt/local-date))]
    (println hello  "Today is:" today ", Options:" opts)
    hello))

(defn square 
  "squares x.
   x may be a number, when called from code or REPL:
   (square 2)"
  [x]
  {:result (* x x)})

(defn square-cli
  "squares x.
   x may be a map like {:value 16}. 
   This is how to call it from cli:
   clj -X deps-tutorial.main/square :value 16"
  [x-map]
  (let [s (square (or (:value x-map) 0))]
    (println s)
    s))

; executed when loaded - useful for scripting
(println "executing script code... ")


