Try this in the shell

``` shell

# start repl
clj 

# show options
clj --help

# show system settings
clj -Sdescribe

# start repl verbosely
clj -Sverbose

# show deps
clj -Stree
clj -X:deps list
clj -X:deps tree

# find lib versions at repo
clj -X:deps find-versions :lib clojure.java-time/clojure.java-time

# run tests - see deps.edn for config 
clj -M:test

# run continous tests. kaocha has --watch option.
# Change code and see what happens.
clj -M:test --watch

# execute code
clj -M -e "(+ 5 6)"

# execute file as script - basically load it / _i_nit
clj -M -i src/deps_tutorial/main.clj

# run -main in namespace deps_tutorial/main
clj -M -m deps-tutorial.main one_arg

# run a function
clj -X deps-tutorial.main/main
clj -X deps-tutorial.main/main :a "bbb"
clj -X deps-tutorial.main/mainABC #fails
clj -X xxx-deps-tutorial.main/main #fails

# a script has 0 or 1 as return value...
# but not the fn's return value.
clj -X deps-tutorial.main/square-cli :value 44

# use the alias, see deps.edn
clj -A:run
clj -X:run
clj -X:run :a b :c d

# create uberjar
clj -Auberjar # ? not ?
clj -Muberjar

# run clojure with main in java as uberjar
java -cp target/e01-0.1.0.jar clojure.main -m deps-tutorial.main arg
# in order to just run the jar without clojure.main, you may add a 
# manifst with main and AOT compile namespace deps-tutorial.main
# wait for e02

clj -Aoutdated 
# will tell you: use
clj -Moutdated 

# to upgrade the outdated...
clj -Moutdated :upgrade true # DOES NOT WORK??? DO IT BY HAND???


```