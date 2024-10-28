### build clojure - but how? 
# deps, tools.build, deps-new, neil!
It's 2024. Im on MacOS. I'm a hobby programmer. I like clojure. Leiningen was easy. There have been some libs that I wanted to change. They are based on deps. And I could not get them up and running in intellij idea. So I decided to start using deps, because I read that the new deps.edn approach is meant to be simple. I learned: There is a confusing landscape of tools - promising but overwhelming: [overview of all tools](https://github.com/clojure/tools.deps.alpha/wiki/Tools). Practically gives an overview that is also just too much for an deps.edn beginner: [practically clojure-deps-edn](https://github.com/yqrashawn/clojure-deps-edn).
So I thought... Can't be that hard. Just start.
I tried cli/deps.edn without having read that much and struggeled ALL THE TIME AGAIN AND AGAIN:
- do I have to understand concepts or libs except source-paths, dependencies and a build-script?
UNFORTUNATELY YES!
- what tools / libs / templates should I use? [deps.edn](https://clojure.org/guides/deps_and_cli) [build.clj](https://clojure.org/guides/tools_build) - [practically template](https://github.com/practicalli/clojure-cli-config) - [build-clj](https://github.com/seancorfield/build-clj) - [deps.clj](https://github.com/borkdude/deps.clj) - [clj-new?](https://github.com/seancorfield/clj-new) - [deps-new?](https://github.com/seancorfield/deps-new) - [bb neil](https://github.com/babashka/neil/)
IT TOOK ME AGES TO READ ALL THAT. 
- when and how to use -S -M -A -X -T? Where to get the basic examples - because the spec does not speak to me?
THERE IS NO PLACE FOR BEGINNERS. THERE ARE MANY INCOMPLETE INOFFICIAL PLACES FOR BEGINNERS - THAT CONFUSES BEGINNERS. E.g. 
  - [clojure-beginners.md: no deps info](https://gist.github.com/yogthos/be323be0361c589570a6da4ccc85f58f)
  - [calva zero install gitpod: no deps info](https://gitpod.io/#https://github.com/PEZ/get-started-with-clojure)
  - [clojurenewbieguide: no deps info](https://www.clojurenewbieguide.com/) 
  - [starting with clojure: no info - but link](https://grison.me/2020/04/04/starting-with-clojure/) 
  - [cli-tools explained: not for newbies](https://betweentwoparens.com/blog/what-are-the-clojure-tools/)
- should I install a tool? Or use an alias in deps.edn? Or place it in build.clj?
TOO MANY OPTIONS FOR A BEGINNER!
- how to remember that many commands and options? 
ALLWAYS HAVING TO LOOK UP DOCs WILL KILL ME...
- using templates, like e.g. [luminus](https://luminusweb.com/)? KIT seemed ok. Having ad a look to [practically site](https://practical.li/clojure/clojure-cli/projects/templates/practicalli/) and [deps-new](https://practical.li/clojure/clojure-cli/projects/templates/practicalli/) killed me.
For luminus/kit, the difference from lein to clj looks like that:
```shell
$ lein new luminus my-app
$ cd my-app
$ lein run
Started server on port 3000
```
Now, with clj it looks like this:
```shell
$ clojure -Ttools install com.github.seancorfield/clj-new '{:git/tag "v1.2.404"}' :as clj-new
$ clojure -Tclj-new create :template io.github.kit-clj :name yourname/guestbook
# and you are not yet up and running...
```
AT LEAST IT SEEMS NEITHER EASY NOR SIMPLE


So I wrote my insights down and made examples from basics to increaseingly complicated.
The idea was to learn it and finally use it without any further roadblocks.
Let`s see... 

## Overview
Install the clojure cli and check, if it's working: 
``` shell

# there is a special tap... Use that:
brew install clojure/tools/clojure

# this also works - but attempts to update to the latest version of Java... 
brew install clojure

# clj or clojure. clj wraps up- and down-key for history...
clj --version
```
### e00: how to (automatically) migrate lein's project.clj to deps.edn ?
- [ ] MOVE TO e10 or something

[lein2deps](https://github.com/borkdude/lein2deps) may be an idea...
I created a lein project with
```shell
lein new app e00
cd e00
```
Then installed lein2deps as tool:
```clojure -Ttools install-latest :lib io.github.borkdude/lein2deps :as lein2deps```

It worked, **BUT** it did for example, not create the uberjar target. It just created deps.edn with :paths and :deps. Especially a complicated lein project with many profiles for shadow-cljs won't work.  

By the way: Removing a tool works like that:
```clj -Ttools remove :tool lein2deps```

### e01: deps.edn - the very basics
**deps.edn** is the file where source-paths (:paths), dependencies (:deps) and aliases (:aliases) of a project are configured. Those :paths :deps and :aliases are used from the clj commands to do things like starting repl, run tests, create jar, etc.   

Example:

``` clojure
;; deps.edn
{:paths   ["src"]
 :deps    {org.clojure/clojure {:mvn/version "1.12.0"}}
 :aliases {:test {:extra-paths ["test"]
                  :extra-deps  {lambdaisland/kaocha {:mvn/version "1.91.1392"}}
                  :main-opts   ["-m" "kaocha.runner"]}
           
           :some {:more _}}}
```
How to start developing?
Assuming you have a file called ```src/deps_tutorial/main.clj```  and ```test/deps_tutorial/main-test.clj``` you may have a look into ```e01/deps.edn```.
There are deps and aliases, so that you may:
``` shell
clj -M:test
#or
clj -M:test --watch
# to automatically rerun tests as you type/save

#or
clj -M -m deps-tutorial.main one_param
# to call the -main fn in  namespace deps-tutorial.main

# or
clj -X deps-tutorial.main/square-cli :value 44
# to call a spedific fn with the map {:value 44} as param

# create uberjar
clj -Muberjar

# run clojure with main in java as uberjar
java -cp target/e01-0.1.0.jar clojure.main -m deps-tutorial.main arg
# in order to just run the jar without clojure.main, you may add a 
# manifst with main and AOT compile namespace deps-tutorial.main
# wait for e02

clj -Aoutdated 
# will tell you that it is deprecated.
# please use:
clj -Moutdated 


```

Based on tomekw`s [deps tutorial](https://github.com/tomekw/cdeps), I created a first example in folder 
```e01```. Go there, have a look at the ```e01/README.md``` for more examples of clj usage and start experimenting.


For more details on clj and deps.edn, see:
- tomekw`s [deps tutorial](https://github.com/tomekw/cdeps), e.g. for uberjar and ancient examples in deps.edn
- [deps.edn guide](https://clojure.org/guides/deps_and_cli)
- [deps.edn reference](https://clojure.org/reference/deps_edn) for all options

To run the example in 
- intellij: just menu File->open and then choose the deps.edn.
  Run the repl by either right clicking on deps.end and start repl or clicking the run symbol.  
- vscode: To run it in vscode, jack-in and choose deps.edn with no alias.

### e02: build.clj - for building everything you may imagine 
**build.clj** is the file where builds are described in the form of executable clojure programs. With deps.edn you may do MANY THINGS. EVEN MOST. With build.clj you can do EVERYTHING. The tools.build library is used for that. First of all, the tools.build lib needs to be included in deps.edn as tool:

``` clojure
;; deps.edn
...
 :aliases {:build {:deps {io.github.clojure/tools.build {:mvn/version "0.10.5"}}
                   :ns-default build}
...          
```

Then, the build.clj may be used with clj. But first, have a look at an example of build.clj:
``` clojure
;; build.clj
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

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[deps-tutorial.main]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'deps-tutorial.main}))          
```
So you may have control about all the details.
How to call those build targets?
You may call it like:
```clj -T:build jar```
```clj -T:build clean```
```clj -T:build uber``` 
To run the uberjar:
```java -jar target/lib-e2-1.2.7-standalone.jar "first param" "second"```

In the special case of a runnable uberjar (build with: ```clj -T:build uber```), you need (:gen-class) and a -main fn in the :main namespace, see example folder
```e02``` and the README.md for more examples.

for details on tools.build, see:
- [tools.build guide](https://clojure.org/guides/tools_build)


### e03: deps-new - creating clj based projects from templates
**deps-new** is a tool for clj to create projects from templates, like lein did. Installing the tool as "new" is done like that:
```
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
```
Don't confuse it with it's predecessor: clj-new!

I created a project from the build-in template 'app' like this:
```clojure -Tnew app :name deps-tutorial/e03```
This creates a subfolder e03, a src and a test folder, a namespace deps-tutorial and a clj file e03.clj with the namespace deps-tutorial.e03 
There is a .gitignore, src and test-folder with initial files, deps.edn and build.clj.

Have a look at the generated skeleton project in folder e03.


With deps-new you may use the build-in templates: ```app lib template scratch```
At the end of the [deps-new README.md](https://github.com/seancorfield/deps-new), there is a description how to use other templates, e.g. [practically templates](https://practical.li/clojure/clojure-cli/projects/templates/).

The syntax may need some explanation, at least. E.g. this
[clerk project template](https://github.com/mentat-collective/clerk-utils/tree/main/resources/clerk_utils/custom) will be used like this:
```
clojure -Sdeps '{:deps {io.github.mentat-collective/clerk-utils {:git/sha "40e7c7bd358c95d1cc96d76eb6ae5099868e500f"}}}' \
-Tnew create \
:template clerk-utils/custom \
:name myusername/my-notebook-project
```

For details on deps-new and templates see: 
- [deps-new](https://github.com/seancorfield/deps-new)
- [practically templates](https://practical.li/clojure/clojure-cli/projects/templates/)

### e04: neil is lein, sort of...
**neil** is lein - but based on clj, deps.edn, tools.build and deps-new - and some other tools.

So let`s try it... How to work with neil?

#### install neil and create a project?
1. install - with and without clj/clojure:
   ```brew install babashka/brew/neil```
   This installs bb (babashka - clojure scripting) and a babashka script called neil.
   ```which neil``` shows ```/opt/homebrew/bin/neil``` at my mac.
1. create a new project named e04
   ```neil new app --name e04```
1. now you can add deps and targets with neil   

#### basics: run, test, build the skeleton?
run the app
  ```
  cd e04
  clj -M:run-m
  ```
test alias with cognitect test-runner
  ```
  clj -M:test
  ```
tools.build and build.clj with ci target (continous integration: run tests and create uberjar)
  ```
  clj -T:build ci
  ```

#### Search libs?
```
neil dep search "time"
```
If you search for tick or time, there are far too much.
This may help:
```
neil dep search time | grep tick
```
which will show
```
:lib tick/tick :version "1.0" :description "Time as a value"
```

#### Add libs?
Just:
```
neil dep add \
:lib tick/tick :version "1.0" :description "Time as a value"
```

or without the :description
```
neil dep add :lib tick/tick :version "1.0"
```

#### Add tests?
``` shell
neil add kaocha # I like that kaocha test runner
# or
neil add test # the standard clojure test runner is included in the app template... 

# tests folder and example tests will also be added, if not already there...
```

#### Run tests?
```shell
# if you added kaocha, you may run tests continously as you type/save
clj -M:kaocha --watch

#otherwise, run tests once
clj -M:test
```

#### Add tools.build support?
```shell
neil add build # this is already done with the app template...
```

#### Upgrade deps versions?
```shell
# first show only 
neil dep upgrade --dry-run
# then do it
neil dep upgrade
```

#### See the transitive dependencies: missing in neil...
Probably, since it is so simple, there is no command in neil for showing the deps tree:
```shell
clj -Stree 
#or
clj -T:deps tree
```

#### See and fix dependency-issues visually
There is a big toolbox for clj collected by [practically](https://github.com/practicalli/clojure-cli-config/blob/main/deps.edn). We add this monster to deps.edn aliases:

```clojure
:graph/deps {:replace-paths []
               :replace-deps  {org.clojure/tools.deps.graph {:mvn/version "1.1.90"}}
               :ns-default    clojure.tools.deps.graph
               :exec-fn       graph
               :exec-args     {:output "project-dependencies-graph.png"
                               :size   true}}
```
To call it, we need to remember:
```
clj -X:graph/deps     
```
For good reasons, that is also not part of neil.


### How to remember all that?
neil is good - but not yet the solution for me,
because many clj commands are still hard to remember.
Solution: use a babashka script called bb.edn in your project folder, like shown in that [neil tutorial](https://blog.michielborkent.nl/new-clojure-project-quickstart.html):
``` clojure
; bb.edn in project root dir
{:tasks
 {:requires ([babashka.fs :as fs])

  test {:doc "Run tests"
        :task (apply clojure "-M:test" *command-line-args*)}

  uber {:doc "Build uberjar"
        :task (clojure "-T:build uber")}}}
```

Go to e04 in terminal and type:
```bb help```
bb.edn will help you to create an easy to use interface 
for your tools that are easy to re-discover or remember...

```bb help``` in ```e04``` provides about this help:
```
test          Run tests once with cognitect test-runner
kaocha        Run tests continously with kaocha
uber          Run tests and if success: build uberjar
tree          Show dependencies as a tree, with clojure -T:deps tree
tree-visual   Show project dependencies as a tree. Requires graphviz. Creates a PNG file called project-dependencies-graph.png
upgrade-check Upgrade dependencies - BUT DRY-RUN ONLY
upgrade       Upgrade dependencies - CHANGE IT
```
try, as an example
```shell
bb kaocha
# or shorter, if you like. See bb.edn on how to
bb k
```

you could even have [tab-completition in zsh](https://book.babashka.org/#tasks) on that...

AND NOW, IT'S FINALLY OK FOR ME.
Because then, I can come back after 6 weeks of working on 'not-programming-related-stuff' and find my way.

for details, see: 
- [neil](https://github.com/babashka/neil)
- [neil tutorial (with bb.edn explained)](https://blog.michielborkent.nl/new-clojure-project-quickstart.html)
- [deps.edn from practically (with everything you may want)](https://github.com/practicalli/clojure-cli-config/tree/main)

But there are still many pieces missing:
- cljs for web development of SPAs
- debugging tools - for clj and cljs files
- deploying as a running web-app to a hosting service
- creating a jar - instead of uber
- deploying as a lib to clojars 
- install a lib locally

### e05: kit - a full fledged clj web app with clj/deps.end
If you want to have a configuration for a web app with a jvm backend and a cljs frontend, you may need kit, the clj-version of luminus. Luminus is based on leiningen. Kit is based on clj.

If you are starting completely from scatch, this is needed to create a project in clojure
``` shell
brew install clojure/tools/clojure
brew install babashka/brew/neil
neil new io.github.kit-clj/kit e06/app
```


Then start intellij with cursive.
"File->Open" Navigate to the deps.edn in the project.
Add: ```-A:dev``` to the run-configuration. 

This gives you something like in e06.
But still, there are some things missing: 
- cljs missing.
- bb.end is missing some things, like e.g.
  - search for libs
  - show recursively deps
  - find outdated libs

see: [kit](https://yogthos.net/posts/2022-01-08-IntroducingKit.html)

### e06: kit configured with cljs and more

### e07: bringing all together...
see:
- [all the tools in practically deps.edn](https://github.com/practicalli/clojure-cli-config/blob/main/deps.edn)
- [video practically](https://www.youtube.com/watch?v=u5VoFpsntXc)
- [video Sean Corfield](https://youtu.be/CWjUccpFvrg?si=cagzN032WH5wjmn7)

### e08: my own template
creating my own project template...
[deps-new templates](https://github.com/seancorfield/deps-new?tab=readme-ov-file#templates)


### e08: just a cljs project

??? https://github.com/filipesilva/create-cljs-app

# the heros:

## cognitect, alex miller

## sean corfield
[deps-new - deps only](https://github.com/seancorfield/deps-new)
[clj-new - templates like lein](https://github.com/seancorfield/clj-new)
[outdated build-clj](https://github.com/seancorfield/build-clj)

## borkdude
[lein2deps](https://github.com/borkdude/lein2deps)
[neil](https://github.com/babashka/neil)
To install [borkdudes deps.clj](https://github.com/borkdude/deps.clj) instead of the bash/powershell based clj tools:
```
$ curl -sL https://raw.githubusercontent.com/borkdude/deps.clj/master/install > install_clojure
$ chmod +x ./install_clojure
$ ./install_clojure --as-clj
```
The native executable for the clojure clj tools may be especially useful for Windows users...
- [clj win installer](https://github.com/casselc/clj-msi?tab=readme-ov-file)
- [deps.clj](https://github.com/borkdude/deps.clj)


## practically
[tutorial](https://practical.li/clojure/clojure-cli/projects/)
[practically templates](https://practical.li/blog-staging/posts/create-deps-new-template-for-clojure-cli-projects/)


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
