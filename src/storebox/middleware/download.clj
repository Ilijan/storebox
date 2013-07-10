(ns storebox.middleware.download
  (:use ring.util.response))

(def file-separator "/")   ;; REVIEW: there is some constant in the java File or somewhere else
                                ;;   maybe it will do the job. Not sure if it have meaning only in Java

(declare -send-the-whole-file -send-chunked-file)

(defn parse-int [str]
  (Integer/parseInt str))

(defn download-handler [root-dir path params]
  (cond
    (not (seq params)) (-send-the-whole-file root-dir path)
    (and (params "offset") (params "length")) (-send-chunked-file root-dir
                                                                  path
                                                                  (parse-int (params "offset"))
                                                                  (parse-int (params "length")))

    :else (throw (Exception. "cannot handle download request"))))

(defn -send-the-whole-file [root-dir path]
  (file-response path {:root root-dir :index-files? false}))
  ; (response (str root-dir "\n" path)))

(defn -read-part-file [root-dir path offset read-size]
  (with-open [rdr (clojure.java.io/reader (str root-dir file-separator path))] ;; NOTE: returns BufferedReader check read(char[] cbuf int off int len)
    (let [cbuf (char-array read-size)]
      (.skip rdr offset)
      (.read rdr cbuf 0 read-size)
      (seq cbuf))))

(defn -send-chunked-file [root-dir path offset read-size]
  (response (apply str (-read-part-file root-dir path offset read-size)))) ;; REVIEW: should it return str when it's binary ?
  ; (response (str root-dir "\n" path "\n" offset "\n" read-size)))
