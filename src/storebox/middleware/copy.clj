(ns storebox.middleware.copy
  (:use ring.util.response
        clojure.java.io
        storebox.paths))

;; NOTE: using this 2 functions instead of destruction
;;       because parameters of type string not boolean
(defn- overwrite [params]
  (or (Boolean/valueOf ("overwrite" params)) true)) ;; REVIEW: parse-bool

(defn- create-dirs [params]
  (or (Boolean/valueOf ("create_dirs" params)) true))

;; REFACTOR: abstraction
(defn- exec-file-op [operation from to overwrite create-dirs]
  (let [from-file (as-file from)
        to-file (as-file to)]
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

(defn- copy-regular-file [^java.io.File from ^java.io.File to]
  (clojure.java.io/copy from to))

(defn- copy-file [& args]
  (apply exec-file-op copy-regular-file args))

;; FIXME: to use abstraction storebox.paths/subsequent-file-seq
(defn- copy-dir-content [from to]
  (let [from-dir (as-file from)
        to-dir (as-file to)]
    (doseq [file (file-seq from-dir)]
      (cond
        (directory-exist? file) (copy-dir-content file to)

        (file-exist? file)
          (copy-regular-file file (concatenate-paths to-dir (.getName file)))

        :else (throw (Exception. "unsupported file type"))))))

(defn- copy-dir [& args]
  (apply exec-file-op copy-dir-content args))

(defn copy-handler [root-dir {from "from_path" to "to_path" :as params}]
  (let [from (concatenate-paths root-dir from)
        to (concatenate-paths root-dir to)]
    (cond
      (directory-exist? from-path) (copy-dir from-path to-path (overwrite params) (create-dirs params))

      (file-exist? from-path) (copy-file from-path to-path (overwrite params) (create-dirs params))

      ;; other files
      :else (throw (Exception. "unsupported file type")))))
