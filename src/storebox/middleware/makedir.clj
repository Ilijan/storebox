(ns storebox.middleware.makedir
  (:use clojure.java.io
        storebox.paths))

(defn makedir-handler [root-dir path {create-dirs "create_dirs"}]
  (let [dir-path (-> (concatenate-paths root-dir path)
                     (as-file))]
    (when-not (if create-dirs (.mkdirs dir-path) (.mkdir dir-path)) ;; REVIEW: if there are subsequent non existent dirs
        (throw (Exception. "directory not created")))))
