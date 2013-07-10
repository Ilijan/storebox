(ns storebox.paths)

(def file-separator "/")   ;; REVIEW: there is some constant in the java File or somewhere else
                           ;;   maybe it will do the job. Not sure if it have meaning only in Java

;; TODO: make all dashes like '/'?, remove unnecessary dashes '/'
(defn concatenate-paths [& args]
  (clojure.string/join file-separator args))