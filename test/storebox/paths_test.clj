(ns storebox.paths-test
  (:require [clojure.test :refer :all]
            [storebox.paths :refer [concatenate-paths]]
            [clojure.string :as str]))

;; REVIEW: moving this function in storebox.pahts and make part of the implementation of conacatenate-paths
(defn independent-separator [path]
  (-> path
      (str/replace "\\" java.io.File/separator)
      (str/replace "/" java.io.File/separator)))

(deftest concatenate-paths-test
  (testing "Independent separator changer"      ;; FIXME: remove
    (is (= (independent-separator "a/a") (str "a" java.io.File/separator "a")))
    (is (= (independent-separator "a\\a") (str "a" java.io.File/separator "a"))))
  (testing "Path concatenation"
    (is (= (concatenate-paths "/some/path" "/rest/of/the/path") (independent-separator "/some/path/rest/of/the/path")))
    (is (= (concatenate-paths "/some/path" "rest/of/the/path") (independent-separator "/some/path/rest/of/the/path")))
    (is (= (concatenate-paths "/some/path/" "/rest/of/the/path") (independent-separator "/some/path/rest/of/the/path")))
    (is (= (concatenate-paths "some/path" "rest/of/the/path") (independent-separator "some/path/rest/of/the/path")))
    (is (= (concatenate-paths "some/path" "rest/of/the/path/") (independent-separator "some/path/rest/of/the/path/")))
    (is (= (concatenate-paths "some/path" "rest/of/the/path/") (independent-separator "some/path/rest/of/the/path/")))
    (is (= (concatenate-paths "/some/../path" "rest/of/the/path/") (independent-separator "/some/path/rest/of/the/path/")))
    (is (= (concatenate-paths "/some/.././path" "rest/of/the/path/") (independent-separator "/some/./path/rest/of/the/path/")))
    ))