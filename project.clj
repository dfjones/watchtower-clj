(defproject watchtower-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [cheshire "5.0.2"]
                 [compojure "1.1.5"]
                 [ring/ring-json "0.2.0"]
                 [hiccup "1.0.2"]
                 [com.novemberain/monger "1.4.2"]
                 [clj-time "0.4.5"]
                 [org.clojure/math.numeric-tower "0.0.2"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler watchtower-clj.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
