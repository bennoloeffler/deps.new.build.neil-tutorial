### build clojure - but how? 
# deps, tools.build, deps-new, neil!
There is an confusing landscape of build tools: leiningen. boot. deps. Some even go the groovy way and use gradle. When you are comming from leinignen and decide to learn deps.edn... Then AGAIN: There is an OVERWHELMING landscape of promising ways to go for it: [overview of all tools](https://github.com/clojure/tools.deps.alpha/wiki/Tools)

So here we go:  This are the tools that I found to work for me without further confusion...

## Overview
``` shell
# this is the command line tool of clojure 
clj --version
```

### deps.edn: the very basics
**deps.edn** is the file where source-paths (:paths), dependencies (:deps) and aliases (:aliases) of a project are configured. This config is used from the clj commands to do things like starting repl or run tests.   
Example:

``` clojure
;; deps.edn
{:paths   ["src"]
 :deps    {org.clojure/clojure {:mvn/version "1.12.0"}}
 :aliases {:test {:extra-paths ["test"]
                  :extra-deps  {lambdaisland/kaocha {:mvn/version "1.91.1392"}}
                  :main-opts   ["-m" "kaocha.runner"]}}}
```
How to start developing?
Assuming you have a file called ```src/deps_tutorial/main.clj```  and ```test/deps_tutorial/main-main.clj``` you may call
```clj -M:test```
and run all tests with kaocha.
Based on tomekw`s [deps tutorial](https://github.com/tomekw/cdeps), I created a first example in folder 
```e01``` go there and start experimenting.


for details on clj and deps.edn, see:
- tomekw`s [deps tutorial](https://github.com/tomekw/cdeps) for uberjar and ancient examples in deps.edn
- [deps.edn guide](https://clojure.org/guides/deps_and_cli)
- [deps.edn reference](https://clojure.org/reference/deps_edn) for all options

### build.clj: for building everything you may imagine 
**build.clj** is the file where builds are described in the form of executable clojure programs. The tools.build library is used for that:
``` clojure
(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'my/lib1)
(def version (format "1.2.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))
```
How to call those build targets?
First, the 
Then, you may call it like:
```clj -T:build jar```
In the special case of the uber jar target, you need (:gen-class) and a -main fn in the :main namespace, see example folder
```e2```

for details on tools.build, see:
- [tools.build guide](https://clojure.org/guides/tools_build)


### deps-new
**deps-new** is a tool for clj to create projects from templates, like lein did. Install:
```
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
```
How to create a new project from templates?
TODO

for details, see: 
- [deps-new](https://github.com/seancorfield/deps-new)

### neil is lein
**neil** is lein - but based on deps, tools.build and deps-new.

How to work with neil?
TODO

for details, see: 
- [neil](https://github.com/babashka/neil)


## Tutorial

## deps.edn for all projects
~/.lein/profile.clj

## borkdude
[lein2deps](https://github.com/borkdude/lein2deps)
[neil](https://github.com/babashka/neil)

## sean corfield
[deps-new - deps only](https://github.com/seancorfield/deps-new)
[clj-new - templates like lein](https://github.com/seancorfield/clj-new)
[outdated build-clj](https://github.com/seancorfield/build-clj)

## practically
[tutorial](https://practical.li/clojure/clojure-cli/projects/)
[practically templates](https://practical.li/blog-staging/posts/create-deps-new-template-for-clojure-cli-projects/)

### project templates
[deps-new templates](https://github.com/seancorfield/deps-new?tab=readme-ov-file#templates)

## all refs
[overview of all tools](https://github.com/clojure/tools.deps.alpha/wiki/Tools)
[]()
[]()
[]()
[]()
[]()
[]()
[]()
[]()
[]()
[neil](https://github.com/babashka/neil)
[tools.build guide](https://clojure.org/guides/tools_build)
[creating libs](https://clojure-doc.org/articles/ecosystem/libraries_authoring/)
