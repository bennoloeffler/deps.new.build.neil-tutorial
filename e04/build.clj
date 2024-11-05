(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'org.clojars.bel/e04)
(def version "0.1.0-SNAPSHOT")
(def main 'e04.e04)
(def target-dir  "target")
(def class-dir (str target-dir "/classes"))
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))


;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (try (b/delete {:path target-dir})
       (catch Exception e (println e))))

(defn test "Run all the tests." [opts]
  (let [basis    (b/create-basis {:project "deps.edn" 
                                  :aliases [:test]})
        cmds     (b/java-command
                  {:basis     basis
                   :main      'clojure.main
                   :main-args ["-m" "cognitect.test-runner"]})
        {:keys [exit]} (b/process cmds)]
    (when-not (zero? exit) (throw (ex-info "TESTS FAILED" {}))))
  opts)

(defn- jar-opts [opts]
  (assoc opts
           :lib        lib
           :version    version
           ;; group/artifact-version.jar is the expected naming convention:
           :jar-file   jar-file;(format "target/%s-%s.jar" lib version)
           :scm  {:tag (str "v" version)}
           :basis      @basis
           :class-dir  class-dir
           :target-dir class-dir ; for b/copy-dir
           :target     target-dir
           :path       target-dir ; for b/delete
           :src-dirs   ["src"]))

; NO COMPILATION, just package sources...
(defn jar [_]
  (test {})
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))


(defn- uber-opts [opts]
  (assoc opts
         :lib lib :main main
         :uber-file uber-file
         :basis (b/create-basis {})
         :class-dir class-dir
         :src-dirs ["src"]
         :ns-compile [main]))

(defn uber "Run the CI pipeline of tests, clean, build and uberjar)." [opts]
  (println "\nRunning TESTS... and if success: UBERJAR")
  (test opts)
  ;(clean nil)
  (let [opts (uber-opts opts)]
    (println "\nCopying source...")
    (b/copy-dir {:src-dirs ["resources" "src"] :target-dir class-dir})
    (println (str "\nCompiling " main "..."))
    (b/compile-clj opts)
    (println "\nBuilding JAR...")
    (b/uber opts))
  opts)

;; Install jar to local Maven repository
(defn install [_]
  (println "\nInstalling to local Maven repository...")
  (jar nil) ;; Ensure the jar is built before installing
  (b/install {:lib lib
              :version version
              :basis @basis
              :class-dir class-dir
              :jar-file jar-file}))



(defn deploy "Deploy the JAR to Clojars." [opts]
  (jar nil)
  (let [{:keys [jar-file] :as opts} (jar-opts opts)]
    (dd/deploy {:installer :remote :artifact (b/resolve-path jar-file)
                :pom-file (b/pom-path (select-keys opts [:lib :class-dir]))}))
  opts)