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
        ring.util.request
        ring.adapter.jetty
        clojure.core        ;;
        compojure.core
        storebox.middleware.helpers
        storebox.middleware.download
        storebox.middleware.upload
        storebox.middleware.copy
        ))

;; TODO: refactor methods invoked with full name space by including the namespace

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

  (POST ["/upload/:path" :path #".+"] [path & args :as all] ;{body :body} ;
    (upload-handler default-root-dir path args (:body-bytes all)))
    ; (response (str path "\n" args "\n" all)))
    ; (response (str "OK\n" (slurp body))))

  (POST ["/copy"] [& params :as all]
    (copy-handler default-root-dir params))
    ; (response (str args "\n" all)))

  (GET ["/makedir/:path" :path #".+"] [path & args :as all] ;; TODO: Add code for this
    (response (str path "\n" args "\n" all)))

  ; (not-found "wrong command")
  )

(def app
  (-> routes-handler
      wrap-params
      wrap-body-str
      wrap-body-copy
      ; wrap-failsafe
      ))

(defn -main [& args]
  (run-jetty app {:port 8080}))
