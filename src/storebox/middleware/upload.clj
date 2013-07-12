(ns storebox.middleware.upload
  (:use ring.util.response
        storebox.paths))

; (defn stream-to-str [stream]
  ; (str (.read stream)))
  ; (with-out-str
    ; (clojure.java.io/copy stream *out*)))

(defn- write-whole-file [root-dir path bytes overwrite]
   ; (with-open [w (clojure.java.io/writer (str root-dir path) :append true)]
    ; (.write w ))
  (if-not (and overwrite (file-exist? (concatenate-paths root-dir path)))
    (let [src bytes
          ; src-content (stream-to-str src)
          dest (str root-dir "/" path)]
      (clojure.java.io/copy src (clojure.java.io/file dest))
      (response "OK"))                                  ;; NOTE: metadata
      ; (assoc (response "OK") :body (slurp src)))
    (throw (Exception. "file exist not overwriting"))))

(defn upload-handler [root-dir path params bytes]
  ; (response (str root-dir "\n" path "\n" params "\n" body)))
  (cond
    (not (seq params)) (write-whole-file root-dir path bytes {:overwrite params})

    :else (throw (Exception. "cannot handle upload request"))))