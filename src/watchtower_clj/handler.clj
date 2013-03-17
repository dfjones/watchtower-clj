(ns watchtower-clj.handler
  (:use compojure.core)
  (:use watchtower-clj.dashboard-handler)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (dashboard))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
