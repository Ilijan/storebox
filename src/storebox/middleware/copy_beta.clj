(ns storebox.middleware.copy-beta
  (:require [me.raynes.fs :as fs])
  (:use storebox.paths))
  
(defn copy-handler [{root-dir :root-dir from "from_path" to "to_path" :as params}]
  (let [from (concatenate-paths root-dir from)
        to (concatenate-paths root-dir to)]
    (fs/copy from to)))