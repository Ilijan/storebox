(ns storebox.middleware.delete
  (:use clojure.java.io
        storebox.paths))

(defn- delete-file-str [file]
  (-> file as-file .delete))

(defn- dir-empty? [^java.io.File dir]
  (not (nil? (first (file-seq dir)))))

;; FIXME: to use abstraction storebox.paths/subsequent-file-seq
(defn- empty-dir [^java.io.File dir]
  (doseq [file (file-seq dir)]
    (cond
      (directory-exist? file) (empty-dir file)

      (file-exist? file) (delete-file-str file)

      :else (throw (Exception. "on delete: unsupported file type")))))

(defn- delete-directory [directory]
  (let [dir (as-file directory)]
    (if (dir-empty? dir)
      (delete-file-str dir)
      (empty-dir dir))))

(defn delete-handler [root-dir path params]
  (let [delete-path (concatenate-paths root-dir path)]
    (cond ;; REFACTOR: this cond occurs in empty-dir, copy-dir-content, copy-handler,... in storebox.middleware.move
      (directory-exist? file) (empty-dir file)

      (file-exist? file) (delete-file-str file)

      :else (throw (Exception. "on delete: unsupported file type")))))
