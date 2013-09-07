(ns storebox.middleware.download-test
  (:require [clojure.test :refer :all]
            [storebox.middleware.download :refer [download-handler]]
            [storebox.tests-helper :refer :all]
            [storebox.paths :refer [concatenate-paths]]))

(deftest download-handler-tests
  (testing "Download"
    (testing "whole file"
      (with-tmp-dir root-dir
        (let [path "/sample_file.txt"
              full-path (concatenate-paths root-dir path)
              resp-body (:body (download-handler root-dir path {}))]
          (println resp-body)
          (is (= (slurp full-path) resp-body)))))
    (testing "part file"
      (with-tmp-dir root-dir
        (let [path "/sample_file.txt"
              full-path (concatenate-paths root-dir path)
              resp-body (:body (download-handler root-dir path {"offset" 3 "length" 5}))
              resp-file-content (slurp full-path)]
          (is (= (subs resp-file-content 3 8) resp-body)))))))