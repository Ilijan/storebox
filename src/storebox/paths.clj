(ns storebox.paths
  (:require (clojure [string :as str])))

;;
(defmulti path-to-str
  #(type %))

(defmethod path-to-str String
  [path]
  path)

(defmethod path-to-str java.io.File
  [path]
  (.getPath path))

(defn concatenate-paths [& args]
  (let [path-separator-pattern (re-pattern (str java.io.File/separator "+"))] ;; REVIEW: windows back-slash ?!?
    (-> (map path-to-str args)
        (str/join java.io.File/separator) ;; FIXME: java.io.File/separator
        (str/replace "." "")
        ; (str/replace "\\" "/")  ; wtf errors
        (str/replace path-separator-pattern java.io.File/separator))))

;;
(defmulti file-exist?
  #(type %))

(defmethod file-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)]
    (and (.exists file) (.isFile file))))

(defmethod file-exist? java.io.File
  [file]
  (and (.exists file) (.isFile file)))

;;
(defmulti directory-exist?
  #(type %))

(defmethod directory-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)]
    (and (.exists file) (.isDirectory file))))

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