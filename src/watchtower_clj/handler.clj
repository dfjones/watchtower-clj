(ns watchtower-clj.handler
  (:use compojure.core)
  (:use ring.middleware.json)
  (:use watchtower-clj.dashboard-handler)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (dashboard))
  (GET "/dashboard/stats" [] (dashboard-stats))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-json-body)
      (wrap-json-response)))
