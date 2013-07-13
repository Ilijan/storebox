(ns storebox.middleware.upload
  (:use ring.util.response
        storebox.paths))

(defn- write-whole-file [root-dir path bytes overwrite]
  (if-not (and overwrite (file-exist? (concatenate-paths root-dir path)))
    (let [src bytes
          dest (str root-dir "/" path)]
      (clojure.java.io/copy src (clojure.java.io/file dest))
      (response "OK"))                                  ;; NOTE: metadata
    (throw (Exception. "file exist not overwriting"))))

(defn upload-handler [root-dir path params bytes]
  (cond
    (not (seq params)) (write-whole-file root-dir path bytes {:overwrite params})

    :else (throw (Exception. "cannot handle upload request"))))