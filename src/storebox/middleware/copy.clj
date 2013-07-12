(ns storebox.middleware.copy
  (:use ring.util.response))

;; NOTE: using this 2 functions instead of destruction 
;;       because parameters of type string not boolean
(defn- overwrite [params]
  (or (Boolean/valueOf ("overwrite" params)) true))

(defn- create-dirs [params]
  (or (Boolean/valueOf ("create_dirs" params)) true))

(defn- copy-fn [operation from to overwrite create-dirs]
  (let [from-file (as-file)
        to-file (as-file)]
    (if overwrite
      (if create-dirs
        (do
          (when-not (make-parents to-file)
            (throw (Exception. "cannot create all directories")))
          (operation from-file to-file))
        (if (directory-exist? (.getParentFile to-file))
          (operation from-file to-file)
          (throw (Exception. "directories does not exist"))))
      (throw (Exception. "file exist not overwriting")))))

(defn- copy-regular-file [^File from ^File to]
  (clojure.java.io/copy from to))

(defn- copy-file [& args]
  (apply copy-fn copy-regular-file args))

(defn- copy-dir-content [from to]
  (let [from-dir (clojure.java.io/file from)
        to-dir (clojure.java.io/file to)]
    (doseq [file (file-seq directory)]
      (cond
        (directory-exist? file) (copy-dir-content file to) ;; REVIEW: .isDirectory

        (file-exist? file)
          (copy-regular-file file (concatenate-paths to-dir (.getName file))) ;; REVIEW: .isFile

        :else (throw (Exception. "unsupported file type"))))))

(defn- copy-dir [& args]
  (apply copy-fn copy-dir-content args))

(defn copy-handler [{from-path "from_path" to-path "to_path" :as params}]
  (cond
    (directory-exist? from-path) (copy-dir from-path to-path (overwrite params) (create-dirs params))

    (file-exist? from-path) (copy-file from-path to-path (overwrite params) (create-dirs params))

    ;; other files
    :else (throw (Exception. "unsupported file type"))))