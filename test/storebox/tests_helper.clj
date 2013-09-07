(ns storebox.tests-helper
  (:require [me.raynes.fs :refer :all]))

(defn create-test-dir []
(let [root (temp-dir "fs-")]
  (mkdir (file root "a"))
  (mkdir (file root "b"))
  (spit (file root "sample_file.txt") "root1234567890qwerty")
  (spit (file root "a" "sample_file_dir_a.txt") "aa")
  (spit (file root "b" "sample_file_dir_b.txt") "bb")
  root))

(defmacro with-tmp-dir [symb & body]
`(let [~symb (create-test-dir)]
   ~@body
   (delete-dir ~symb)))