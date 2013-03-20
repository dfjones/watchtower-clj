(ns watchtower-clj.dashboard-handler
  (:use watchtower-clj.view-dashboard)
  (:use clj-time.core)
  (:require [cheshire.core :refer :all])
  (:refer-clojure :exclude [extend])
  (:require [watchtower-clj.stats :as stats]))

(defn dashboard []
  (view-dashboard {}))

(defn dashboard-stats []
  {:body {:stats (stats/gen-stats-summary (minutes 5) 40)}})

   

