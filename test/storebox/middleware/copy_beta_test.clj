(ns storebox.middleware.copy-beta-test
  (:require [clojure.test :refer :all]
            [storebox.middleware.copy-beta :refer [copy-handler]]
            [storebox.paths :refer [concatenate-paths]]
            [me.raynes.fs :refer :all]))

(defn create-test-dir []
  (let [root (temp-dir "fs-")]
    (mkdir (file root "a"))
    (mkdir (file root "b"))
    (spit (file root "sample_file.txt") "root")
    (spit (file root "a" "sample_file_dir_a.txt") "aa")
    (spit (file root "b" "sample_file_dir_b.txt") "bb")
    root))

(defmacro with-tmp-dir [symb & body]
  `(let [~symb (create-test-dir)]
     ~@body
     (delete-dir ~symb)))

(deftest copy-handler-tests
  (testing "Copying"
    (testing "file"
      (with-tmp-dir root-dir
        (let [from "/a/sample_file_dir_a.txt"
              to "asd.txt"
              to-path (concatenate-paths root-dir to)]
          (copy-handler {:root-dir root-dir "from_path" from "to_path" to})
          (is (file? to-path))
          (is (= (slurp to-path) "aa")))))
    (testing "directory"
      (with-tmp-dir root-dir
        ))))
