; (ns storebox.core
  ; (:gen-class))

; (defn -main
  ; "I don't do a whole lot ... yet."
  ; [& args]
  ; work around dangerous default behaviour in Clojure
  ; (alter-var-root #'*read-eval* (constantly false))
  ; (println "Hello, World!"))

;; When executed, this file will run a basic web server
;; on http://localhost:8080 that will display the text
;; 'Hello World'.

;; When executed, this file will run a basic web server
;; on http://localhost:8080.

(ns storebox.core
  (:use ring.middleware.params
        ring.util.response
        ring.adapter.jetty
        clojure.core        ;;
        compojure.core
        storebox.middleware.helpers
        storebox.middleware.download
        ))

(defn extract-api-call [uri]
  (second (clojure.string/split uri #"/")))

(defn download-request? [request]
  (let [request-method (name (:request-method request))
        api-call (extract-api-call (:uri request))]
    (if (and request-method api-call)
      (and (= (clojure.string/lower-case request-method) "get")
        (= (clojure.string/lower-case api-call) "download"))
      false)))

(defn wrap-download-response [handler]
  (fn [request]
    (let [response (handler request)]
      (if (download-request? request)
        (file-response "README.md" {:root "."})
        response))))

(defn handler [request]
  (response (str (:uri request) "\n" (:query-string request) "\n" (download-request? request))))
  ; (response "<html><body>dfsdfsd</body></html>"))

(defn wrap-shit [handler]
  (fn [request]
    (response "shitttt")))

; (def app
  ; (-> handler
      ; wrap-params
      ; wrap-download-response
      ; wrap-shit
      ; ))

(def default-root-dir ".")

(defroutes routes-handler
  (GET ["/download/:path" :path #".+"] [path & args :as all]
    (download-handler default-root-dir path args))
    ; (response (str path "\n" args "\n" all)))
    ; (file-response path {:root default-root-dir :index-files? false}))
    
  (POST ["/upload/:path" :path #".+"] [path & args :as all]
    )

  ; (not-found "wrong command")
  )

(def app
  (-> routes-handler
      wrap-params
      ; wrap-failsafe
      ))

(defn -main [& args]
  (run-jetty app {:port 8080}))
