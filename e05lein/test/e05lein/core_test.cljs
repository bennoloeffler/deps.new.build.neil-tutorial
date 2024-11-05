(ns e05lein.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [e05lein.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
