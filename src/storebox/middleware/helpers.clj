(ns storebox.middleware.helpers)

(defn wrap-if [handler pred wrapper & args]
  (if pred
    (apply wrapper handler args)
    handler))

(defn wrap-failsafe [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        {:status 500
         :headers {"Content-Type" "text/plain"}
         :body "Something went wrong o_0\nTeam of highly trained monkeys... NO!"}))))

;; TODO: wrap-... methods 

(defn wrap-body-copy [handler]
  (fn [{body :body :as request}]
    (let [byte-output (java.io.ByteArrayOutputStream.)]
      (clojure.java.io/copy body byte-output)
      (let [body-copy (java.io.ByteArrayInputStream. (.toByteArray byte-output))
            body-byte-array (.toByteArray byte-output)
            request-with-new-body (assoc request :body body-copy)
            new-request (assoc request-with-new-body :body-bytes body-byte-array)]
        (handler new-request)))))

(defn wrap-body-str [handler]
  (fn [{bytes :body-bytes :as request}]
    (if bytes
      (let [body-str (apply str (seq (map char bytes)))
            new-request (assoc request :body-str body-str)]
        (handler new-request))
      (handler request))))