Try this in the shell

``` shell

# run fn with parameters as map
# gets transformed to {:name MyName}
clj -X:run :name MyName :age 23

# build jar
# looking into the jar shows:
# No java classes. 
# Only clj sourcecode.
# So not executable by java!
# But usable as lib for clojure project...
clj -T:build jar

# run it - BUT FAILS... classes are missing in the jar...
java -jar target/lib-e2-1.2.7.jar

# build uberjar
clj -T:build uber

# run - with and without arguments.
# DIFFERENT to clj -X:run :name MyName
# no map but just parameters
java -jar target/lib-e2-1.2.7-standalone.jar "first param" "second"

# no map! but 4 parameters
java -jar target/lib-e2-1.2.7-standalone.jar :name MyName :age 23

# clean
clj -T:build clean
```
