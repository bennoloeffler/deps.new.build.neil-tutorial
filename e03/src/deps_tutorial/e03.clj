(ns deps-tutorial.e03
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))

(comment
  (let [home-dir (System/getProperty "user.home")
        zshrc-path (str home-dir "/.zshrc")
        zshrc-txt (slurp zshrc-path)
        installed (clojure.string/includes? zshrc-txt "`bb tasks")
        completition-code "#completition with babashka tasks\n_bb_tasks() {\n    local matches=(`bb tasks |tail -n +3 |cut -f1 -d ' '`)\n    compadd -a matches\n    #_files # autocomplete filenames as well\n}\ncompdef _bb_tasks bb"]
    (if installed
      (println "bb tasks is ALREADY INSTALLED. Not working? Try to restart your shell.")
      (do
        (spit zshrc-path (str zshrc-txt "\n\n" completition-code))
        (println "bb tasks is NOW INSTALLED.\nRestart your shell or do\nsource ~/.zshrc")))))
