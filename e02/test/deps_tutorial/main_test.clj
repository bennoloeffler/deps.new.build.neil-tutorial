(ns deps-tutorial.main-test
  (:require [clojure.test :refer :all]
            [deps-tutorial.main :as main]))

(deftest main-test
  (testing "main"
    (is (= "Hello, world!" (main/-main {:name "Benno"})))))