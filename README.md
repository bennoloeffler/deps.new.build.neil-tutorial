### build clojure 2024 - but how? 
# deps, tools.build, deps-new, neil!
It's 2024. Im on MacOS. I'm a hobby programmer. I like clojure. Leiningen was easy. There have been some libs that I wanted to change. They are based on deps. And I could not get them up and running in intellij idea. So I decided to start using deps, because I read that the new deps.edn approach is meant to be simple. I learned: There is a confusing landscape of tools - promising but overwhelming: [overview of all tools](https://github.com/clojure/tools.deps.alpha/wiki/Tools). Practically gives an overview that is also just too much for an deps.edn beginner: [practically clojure-deps-edn](https://github.com/yqrashawn/clojure-deps-edn).
So I thought... Can't be that hard. Just start.
I tried cli/deps.edn without having read that much and struggeled ALL THE TIME AGAIN AND AGAIN:
- do I have to understand concepts except source-paths, dependencies and a build-script?
UNFORTUNATELY YES!
- what tools / libs / templates should I use? [deps.edn](https://clojure.org/guides/deps_and_cli) [build.clj](https://clojure.org/guides/tools_build) - [practically template](https://github.com/practicalli/clojure-cli-config) - [build-clj](https://github.com/seancorfield/build-clj) - [deps.clj](https://github.com/borkdude/deps.clj) - [clj-new?](https://github.com/seancorfield/clj-new) - [deps-new?](https://github.com/seancorfield/deps-new) - [bb neil](https://github.com/babashka/neil/)
IT TOOK ME AGES TO READ ALL THAT. 
- when and how to use -S -M -A -X -T? Where to get basic examples - because the spec does not speak to me?
THERE IS NO PLACE FOR BEGINNERS. THERE ARE MANY INCOMPLETE, OUTDATED, INOFFICIAL PLACES FOR BEGINNERS - THAT CONFUSES BEGINNERS: 
  - [clojure-beginners.md: no deps info](https://gist.github.com/yogthos/be323be0361c589570a6da4ccc85f58f)
  - [calva zero install gitpod: no deps info](https://gitpod.io/#https://github.com/PEZ/get-started-with-clojure)
  - [clojurenewbieguide: no deps info](https://www.clojurenewbieguide.com/) 
  - [starting with clojure: no info - but link](https://grison.me/2020/04/04/starting-with-clojure/) 
  - [cli-tools explained: not for newbies](https://betweentwoparens.com/blog/what-are-the-clojure-tools/)
- should I install a tool? Or use an alias in deps.edn? Or place it in build.clj?
TOO MANY OPTIONS FOR A BEGINNER!
- how to remember that many commands and options? 
ALLWAYS HAVING TO LOOK UP DOCs WILL KILL ME...
- how to use templates, like e.g. [luminus](https://luminusweb.com/)? The deps-version of it called [kit](https://kit-clj.github.io/) seems ok. Had a look to templates at [practically site](https://practical.li/clojure/clojure-cli/projects/templates/practicalli/) and [deps-new](https://practical.li/clojure/clojure-cli/projects/templates/practicalli/), [cljs-new](https://github.com/seancorfield/clj-new). That almost killed me. For luminus/kit, the difference from lein to clj looks like that: 
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
  # TODO and you are not yet up and running...
  ```
It may be simple ;-) But for me it seems impossible to remember. 
So I wrote my insights down and made examples from basics to increaseingly complicated.
The idea was to learn deps.edn and tools.build and finally use it without any further time consuming roadblocks.
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

for every OS, see Eric Normand's
[How to install Clojure](https://ericnormand.me/guide/how-to-install-clojure)

### example e01: deps.edn - the very basics
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
# wait for example e02

clj -Aoutdated 
# will tell you that it is deprecated.
# please use:
clj -Moutdated 

# show classpath for this alias
clj -Spath -M:test 

```

Based on tomekw`s [deps tutorial](https://github.com/tomekw/cdeps), I created a first example in folder 
```e01```. Go there, have a look at the 
```
e01/README.md
``` 
for more examples of clj usage and start experimenting.


For more details on clj and deps.edn, see:
- tomekw`s [deps tutorial](https://github.com/tomekw/cdeps), e.g. for uberjar and ancient examples in deps.edn
- [Volodymyr Kozieievs tutorial](https://kozieiev.com/blog/clojure-cli-tools-deps-deps-edn-guide/)
- [deps.edn guide](https://clojure.org/guides/deps_and_cli)
- [deps.edn reference](https://clojure.org/reference/deps_edn) for all options

To run the example in 
- intellij: just menu File->open and then choose the deps.edn.
  Run the repl by either right clicking on deps.end and start repl or clicking the run symbol.  
- vscode: To run it in vscode, jack-in and choose deps.edn with no alias.

### example e02: build.clj - for building everything you may imagine 
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


### example e03: [deps-new](https://github.com/seancorfield/deps-new) - creating clj based projects from templates
**deps-new** is a tool for clj to create projects from templates, like lein did. Installing the tool as "new" is done like that:
```
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new
```
Don't confuse it with it's predecessor: clj-new!
[clj-new](https://github.com/seancorfield/clj-new) is able to use templates from leiningen, boot and clj. 

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
- [very short and good video tutorial](https://www.youtube.com/watch?v=2K7cQ8UYRzo)
- [deps-new](https://github.com/seancorfield/deps-new)
- [practically templates](https://practical.li/clojure/clojure-cli/projects/templates/)

### example e04: neil is lein, sort of...
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

#### See the transitive dependencies?
Missing in neil. Probably, since it is so simple, there is no command in neil for showing the deps tree:
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
For good reasons that is also not part of neil.


### How to remember? Well babashka and bb.edn...
neil is good - but not yet THE solution for me,
because many clj commands are hard to remember.
neil helps me to create the project - but not with all the rest.
I did not like to learn GNU make. And 'just' is not clojure. 
So I use a babashka script called bb.edn in your project folder, like shown in that [neil tutorial](https://blog.michielborkent.nl/new-clojure-project-quickstart.html):
``` clojure
; bb.edn in project root dir
{:tasks
 {:requires ([babashka.fs :as fs])

  test {:doc "Run tests"
        :task (apply clojure "-M:test" *command-line-args*)}

  uber {:doc "Build uberjar"
        :task (clojure "-T:build uber")}}}
```
You are calling those command-shortcuts from inside e04 like:
```
cd e04
bb test
```

bb.edn will help you to create an easy to use interface 
for your tools that are easy to re-discover or remember...

```shell
# type
bb help
# in folder e04
# will provides this type of help:

help          show help
clean         clean project from all build artefacts.
test          Run tests once with cognitect test-runner
kaocha        Run tests continously with kaocha
main          Run main function. Did not call it 'run' because it would override the built-in run task
jar           create jar - just package sources
uber          Run tests and if success: build uberjar
tree          Show transitive dependencies as a tree, with clojure -T:deps tree
tree-visual   Show project dependencies as a tree. Requires graphviz. Creates a PNG file called project-dependencies-graph.png
upgrade-check Check for outdated dependencies - dry run only
upgrade       Upgrade dependencies - change it in deps.edn
search        Search lib at Maven/clojars: bb search text
install       create pom.xml, jar and install as lib in local maven cache repo ~/.m2/...
deploy        test, jar and deploy to clojars
...           ...
```
try, as an example
```shell
bb kaocha
# or shorter, if you installed the completition, see e04/README.md
bb k<tab>
```
[tab-completition in zsh for bb tasks](https://book.babashka.org/#tasks)

#### lib: deploy to clojars, use and install locally
If you want to use e04 locally yourself, e.g. in your projects or publish e04 as lib to clojars, how to do that?
Deploying to clojars is easy:
```shell
# First, you need to set the clojars credentials, eg in .zshrc:
# export CLOJARS_USERNAME="bruno.loffi@gmz.uk" 
# export CLOJARS_PASSWORD="CLOJARS_261***"
# Then provide the version and unique path in build.clj:
# (def lib 'org.clojars.bel/e04)
# (def version "0.1.0-SNAPSHOT")
# Then
bb deploy 
# will do the deployment
```

From now, you and everybody else may use it as dependency:
```shell

# Try by calling that somewhere outside folder e04.
# If you try inside folder e04, the lib will be downloaded,
# but your source code gets priority, because it is in classpath.  
clojure -Sdeps '{:deps {org.clojars.bel/e04 {:mvn/version "0.1.0-SNAPSHOT"}}}' -M -m e04.e04
```

Now change something in the sourcecode, e.g.:
```clojure

# change the src/e04/e04.clj 
(println (str "Hello, brave new " (or (:name data) "World") "!"))
```
This time, install the lib at your machine only:

```shell
bb install # will install the lib only on your machine
```
Run it again (outside folder e04)
```shell
clojure -Sdeps '{:deps {org.clojars.bel/e04 {:mvn/version "0.1.0-SNAPSHOT"}}}' -M -m e04.e04
``` 

So you see **Hello, brave new World!**
All other users, downloading from clojars, will still see **Hello, World!**, but you are using locally an updated version of your lib in all your projects, that consume that lib.

**Alternatively and even better:** As a new and very simple way to use local libs, you may give a local path as dependency.
From the deps.edn of e05, which is in a neighbour-folder, you may say:
```clojure
{:deps
 {org.clojars.bel/e04 {:local/root "../e04"}}}
```

**AND NOW, IT'S FINALLY OK FOR ME.**
Because now, I can come back after 6 weeks of programming abstinence and find my way.

for details, see: 
- [neil](https://github.com/babashka/neil)
- [neil tutorial (with bb.edn explained)](https://blog.michielborkent.nl/new-clojure-project-quickstart.html)
- [deps.edn from practically (with everything you may want)](https://github.com/practicalli/clojure-cli-config/tree/main)
- [tools.deps cookbook](https://clojure-doc.org/articles/cookbooks/cli_build_projects/)
- [alternative to bb: a tool called 'just' in a full example](https://caveman.mccue.dev/)

But there are still some pieces missing:
- cljs for web development of SPAs

### example e05: cljs and re-frame

A template for deps is there:
https://github.com/omnyway-labs/re-frame-template

Unfortunately, the original lein version seems maintained...
https://github.com/Day8/re-frame-template


Well, lets give it a try: To create a project from that omnyway-labs/re-frame-template, I typed:
```
clojure -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "1.0.199"}}}' \
        -M -m clj-new.create re-frame-template bel/e05
```
Yes..., I'm using seancorfield/clj-new - and not deps-new!

To run the app, do:
```
npm install
shadow-cljs -A dev watch app
```
**First problem**: compile failed with some weired dependency problems. My conclusion: The dependencies were too old to work. So...
```
neil dep upgrade --dry-run
```
showed many outdated deps - almost all.

Had to fix it by hand, because
**Second Problem**
```
neil dep upgrade
```
did fail. This seems because of the weird writing of deps:
```clojure
org.clojure/clojure #:mvn{:version "1.12.0"}

; changing it to this will make neil dep upgrade work... 
org.clojure/clojure {:mvn/version "1.12.0"}
```
**Third Problem**
Finally a deprecation warning in the cljs compiler and error in the browser said about: The render function is not any more in reagent.core, but in reagent.dom.
So I changed:
``` clojure
(ns bel.e05.core
    (:require
     [reagent.core :as reagent] ; <-- core
     [re-frame.core :as re-frame]

# to
(ns bel.e05.core
    (:require
     [reagent.dom :as reagent] ; <-- dom
     [re-frame.core :as re-frame]
```
Then it worked... There MUST be a newer cljs/re-frame template?
I gave up on this and hoped for kit...
Although there are plenty of templates to start with a cljs project:
- [thheller tutorial about cljs and deps.edn](https://code.thheller.com/blog/shadow-cljs/2024/10/18/fullstack-cljs-workflow-with-shadow-cljs.html)
- [templates on the cljs homepage](https://clojurescript.org/guides/project-templates)
- [MOST MATURE LEIN template with many +options](https://github.com/day8/re-frame-template)
- [create-cljs-app](https://github.com/filipesilva/create-cljs-app)

Finally, I tried:

```shell
lein new re-frame e05lein +test +10x
npm install
npx shadow-cljs watch app browser-test karma-test
```
For all aliases see package.json.
For tests: http://localhost:8290/
For app: http://localhost:8280/

That just worked - by the way: 
It does not include a lein project.clj ;-)

TODO try this:
https://github.com/kees-/re-marfer


### example e06: kit - a full fledged clj/cljs SPA web app with clj/deps.end
If you want to have a configuration for a web app with a jvm backend and a cljs frontend, you may use kit, the clj-version of luminus. Luminus is based on leiningen. Kit is based on clj. There was a very, very good book, guiding you through a complete SPA webapp. Probably, the kit documentation is a similar good full docu of all aspects of a web app.

If you are starting completely from scatch, this is needed to create a project in clojure
``` shell
# brew install clojure/tools/clojure ; already done - not needed
# brew install babashka/brew/neil    ; already done - not needed

# DOES NOT WORK with parameters:
# neil new io.github.kit-clj/kit e06/app --xtdb # NOOOO :args '[+full]'
# it seems to include libs (sometimes)
# but not the template code


# first, install clj-new...
clojure -Ttools install-latest :lib com.github.seancorfield/clj-new :as clj-new

# then
clojure -Tclj-new create :template io.github.kit-clj :name bel/e06 
# :args '[+xtdb +sql]'
```
[+full] includes: +xtdb, +hato, +metrics, +quartz, +redis, +repl, +selmer, and +sql

Have a look to the [profiles, that may be used at project creation time](https://kit-clj.github.io/docs/profiles.html)

Then, start a repl with :dev profile:
```
clj -M:dev
```

To add cljs, database and some other magic, you need to run in the  repl:
```clojure
(kit/sync-modules) ; do once
(kit/list-modules) ; see, whats available
(kit/install-module :kit/html) ; otherwise, there will be an error with cljs ???
(kit/install-module :kit/cljs) ; install something
```
Have a look at e06/README.md.

Then start a continous frontend compilation mode of shadow-cljs
```
shadow-cljs -A dev watch app
``` 


Then, re-start a backend repl, 
e.g.
```
clj -M:dev
```
Or start intellij with cursive.
"File->Open" Navigate to the deps.edn in the project.
Add: -M:dev to the run-configuration. 
Then (go) in the repl starts the server... 

- Having added sql as module, I had to fix the connect string:
"jdbc:postgresql://localhost/template1?user=postgres&password=xr...r"
where template1 is the database name.
- then created a migration:
(migratus.core/create (:db.sql/migrations state/system) "add-guestbook-table")
- ERROR MESSAGE: Execution error (IllegalArgumentException) at integrant.core/try-build-action (core.cljc:332).
No method in multimethod 'init-key' for dispatch value: :reitit.routes/pages

STOPPED HERE. I should have read this and started from the very first step:
[Your First Application - in KIT Docu](https://kit-clj.github.io/docs/guestbook.html) 

Aditional stuff [kit introduction](https://yogthos.net/posts/2022-01-08-IntroducingKit.html)

Still more upfront work to be done than a beginner can stand for a simple SPA... Couldnt there be a SPA configuration with a file based backend, a user-management, so that i can just start with a web application?

### TODO example e07: bringing all together

I'd like to have a fullstack template with components that fit together and are either finished or maintained:
- A template like kit
- reasonable css as default (bulma, e06:simple.css)
- routing 
- re-frame
- user authentication and roles
- xtdb on the server side - with examples for user management
- menu
- client side routing
- shadow-cljs
- all aliases needed to work
- build.clj with ci and all single steps (clean, jar, ... deploy)
- bb.edn with everything to lookup

There is something:

- Kind of... [Hammock Web Development](https://clyfe.infinityfreeapp.com/book.html?i=2#_functional_web_building_blocks) - clyfe 
- This is it: https://www.shipclojure.com/, even email and payment
- my old example: https://github.com/bennoloeffler/decision-konsent




### example e08: electric - the new shit
https://github.com/hyperfiddle/electric-v2-starter-app
I just did:
```
git clone https://github.com/hyperfiddle/electric-v2-starter-app.git e08
```
then started a repl with dev profile and called 
```
(dev/-main)
```

then:
http://localhost:8080/

have a look at all the demos.
Well: nice. but faaaaaar to complicated to start.

## install as tool, alias (also in ~/.clojure/deps.edn) or use directly?
As an example, we install "deps-new lib" in different ways:

As tool:
```shell
clojure -Ttools install-latest :lib io.github.seancorfield/deps-new :as new

# to use
clj -Tnew app :name deps-tutorial/e03

# to uninstall
clj -Ttools remove :tool new```
```

As alias - could be done in ~/.clojre/deps.edn to have it everywhere:
``` clojure
;; in deps.edn
{:aliases
 {:new
  {:deps {io.github.seancorfield/deps-new
          {:git/tag "v0.8.0" :git/sha "2f96530"}}
   :ns-default deps-new}}}   
```

```shell
# then use on shell
clj -Anew app :name deps-tutorial/e03
```

As direct dep in the call - totally without install:    
```shell
clj -Sdeps '{:deps {seancorfield/deps-new {:git/tag "v0.8.0" :git/sha "2f96530"}}}' -m deps-new app org.clojars.myname/projectname
```



# Helpful links:

## sean corfield
- [deps-new - deps only](https://github.com/seancorfield/deps-new)
- [clj-new - templates like lein](https://github.com/seancorfield/clj-new)
- [deps-new templates](https://github.com/seancorfield/deps-new?tab=readme-ov-file#templates)
- [video Sean Corfield deps.edn](https://youtu.be/CWjUccpFvrg?si=cagzN032WH5wjmn7)
- [outdated build-clj](https://github.com/seancorfield/build-clj)

## borkdude
- [lein2deps](https://github.com/borkdude/lein2deps)
- [neil](https://github.com/babashka/neil)
- To install [borkdudes deps.clj](https://github.com/borkdude/deps.clj) instead of the bash/powershell based clj tools:
  ```
  $ curl -sL https://raw.githubusercontent.com/borkdude/deps.clj/master/install > install_clojure
  $ chmod +x ./install_clojure
  $ ./install_clojure --as-clj
  ```
- The native executable for the clojure clj tools may be especially useful for Windows users...
  - [clj win installer](https://github.com/casselc/clj-msi?tab=readme-ov-file)
  - [deps.clj](https://github.com/borkdude/deps.clj)


## practically
- [tutorial](https://practical.li/clojure/clojure-cli/projects/)
- [video practically](https://www.youtube.com/watch?v=u5VoFpsntXc)
- [practically templates](https://practical.li/blog-staging/posts/create-deps-new-template-for-clojure-cli-projects/)
- [all the tools in practically deps.edn](https://github.com/practicalli/clojure-cli-config/blob/main/deps.edn)



## all refs
- [overview of all tools](https://github.com/clojure/tools.deps.alpha/wiki/Tools)
- [creating libs](https://clojure-doc.org/articles/ecosystem/libraries_authoring/)



