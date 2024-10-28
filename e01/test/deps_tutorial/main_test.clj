(ns deps-tutorial.main-test
  (:require [clojure.test :refer :all]
            [deps-tutorial.main :as main]))

(deftest -main-test
  (testing "-main"
    (is (= "Hello, world!" (main/-main nil)))))

(deftest square-test
  (let [check-square (fn [n] (= (* n n) (:result (main/square n))))]
    (testing "square"
      (is (check-square -1))
      (is (check-square 0))
      (is (check-square 1))
      (is (check-square 0.000001))
      (is (check-square 10000000000000000000000)))))
  
(deftest another-test
  (testing "WILL FAIL"
    (is (= 1 0))))
