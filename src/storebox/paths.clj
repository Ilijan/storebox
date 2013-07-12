(ns storebox.paths
  (:require (clojure [string :as str])))

;; FIXME: File/separator
(def file-separator "/")   ;; REVIEW: there is some constant in the java File or somewhere else
                           ;;         maybe it will do the job. Not sure if it have meaning only in Java

;;
(defmulti path-to-str
  #(type %))

(defmethod path-to-str String
  [path]
  path)

(defmethod path-to-str File
  [path]
  (.getPath path))

(defn concatenate-paths [& args]
  (-> (map path-to-str args)
      (str/join file-separator) ;; FIXME: File/separator
      (str/replace "." "")
      ; (str/replace "\\" "/")
      (str/replace #"/+" "/")))

;;
(defmulti file-exist?
  #(type %))

(defmethod file-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)
    (and (.exists file) (.isFile file))))

(defmethod file-exist? File
  [path]
  (and (.exists file) (.isFile file)))

;;
(defmulti directory-exist?
  #(type %))

(defmethod directory-exist? String
  [path]
  (let [file (clojure.java.io/as-file path)
    (and (.exists file) (.isDirectory file))))

(defmethod directory-exist? File
  [path]
  (and (.exists file) (.isDirectory file)))

;;
(defmulti directory-exist?
  #(type %))

(defmethod path-exist? String
  [path]
  (.exists (clojure.java.io/as-file path)))

(defmethod path-exist? File
  [path]
  (.exists path))

;;
(defn )