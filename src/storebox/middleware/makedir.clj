(ns storebox.middleware.makedir
  (:use clojure.java.io))

(defn makedir-handler [root-dir path]
  (let [dir-path (-> (concatenate-paths root-dir path)
                     (as-file))]
    (when-not (.mkdir dir-path)  ;; REVIEW: if there are subsequent not existient dirs
      (throw (Exception. "directory not created")))))