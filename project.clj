(defproject storebox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.0-RC1"]
                 [compojure "1.1.5"]
                 [me.raynes/fs "1.4.4"]]
  ; :dev-dependencies [[ring-serve "0.1.2"]]
  :profiles {:dev {:dependencies [[ring-serve "0.1.2"]]}}
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler storebox.core/app}
  :main storebox.core)
