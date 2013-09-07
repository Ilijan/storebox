(ns storebox.paths
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;;
(defmulti path-to-str
  #(type %))

(defmethod path-to-str String
  [path]
  path)

(defmethod path-to-str java.io.File
  [path]
  (.getPath path))

(defn trim-slashes [str slash]
  (loop [last-string (str/replace str "\\" "/")]
    (let [current-string (str/replace last-string "//" "/")]
      (if (= current-string last-string)
        (str/replace current-string "/" java.io.File/separator)
        (recur current-string)))))

(defn concatenate-paths [& args]
  (-> (str/join java.io.File/separator (map path-to-str args))
      (str/replace ".." "")
      (str/replace "\\" "/")  ; wtf errors
      (trim-slashes java.io.File/separator)))

;;
(defmulti file-exist?
  #(type %))

(defmethod file-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)]
    (and (.exists file) (.isFile file))))       ;; FIXME: .isFile checks for existence

(defmethod file-exist? java.io.File
  [file]
  (and (.exists file) (.isFile file)))

;;
(defmulti directory-exist?
  #(type %))

(defmethod directory-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)]
    (and (.exists file) (.isDirectory file))))    ;; FIXME: .isFile checks for existence

(defmethod directory-exist? java.io.File
  [file]
  (and (.exists file) (.isDirectory file)))

;;
(defmulti path-exist?
  #(type %))

(defmethod path-exist? String
  [path]
  (.exists (clojure.java.io/as-file path)))

(defmethod path-exist? java.io.File
  [path]
  (.exists path))

;; subsequent-file-seq like file-seq but with the subfile
; (defn- subsequent-file-seq [file]
  ; (let [dir-seq (file-seq (clojure.java.io/as-file file))]
    ; (with-bindings* {#'*dir-seq* dir-seq}
      ; )))