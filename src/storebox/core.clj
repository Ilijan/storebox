(ns storebox.core
  (:use ring.middleware.params
        ring.util.response
        ring.util.request
        ring.adapter.jetty
        clojure.core        ;;
        compojure.core
        storebox.middleware.wrap-helpers
        storebox.middleware.download
        storebox.middleware.upload
        storebox.middleware.copy
        storebox.middleware.makedir
        storebox.middleware.move
        ))

;; TODO: refactor methods invoked with full name space by including the namespace

(def default-root-dir ".")

;; REVIEW: args one hash only?
(defroutes routes-handler
  (GET ["/download/:path" :path #".+"] [path & args :as all]
    (download-handler default-root-dir path args))

  (POST ["/upload/:path" :path #".+"] [path & args :as all] ;{body :body} ;
    (upload-handler default-root-dir path args (:body-bytes all)))

  (POST ["/copy"] [& params :as all]
    (copy-handler default-root-dir params))

  (PUT ["/makedir/:path" :path #".+"] [path & args :as all]
    (makedir-handler default-root-dir path args))

  (DELETE ["/delete/:path" :path #".+"] [path & args :as all]
    (delete-handler default-root-dir path args))

  (POST ["/move"] [& args]
    (move-handler args))  ;; TODO: implement handler

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
