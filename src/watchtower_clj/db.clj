(ns watchtower-clj.db
  (:use clj-time.core)
  (:require [monger.core :as mg])
  (:require [monger.collection :as mc])
  (:require monger.joda-time)
  (:refer-clojure :exclude [extend sort find])
  (:use monger.query)
  (:use monger.operators)
  (:import [com.mongodb MongoOptions ServerAddress]))

(mg/connect!)

(mg/set-db! (mg/get-db "Watchtower"))

(def crawl-queue "crawl_queue")
(def crawl-records "crawl_records")
(def url-stats "url_stats")

(defn store-url-stat [stat]
  (mc/insert url-stats stat))

(defn in-crawl-queue [url]
  (> (mc/count crawl-queue {:url url}) 0))

(defn add-to-crawl-queue
  ([url] (add-to-crawl-queue url (now)))
  ([url date]
    (if (not (in-crawl-queue url))
      (mc/insert crawl-queue
        {
          :url url
          :date date
        }))))

(defn remove-from-crawl-queue [url]
  (mc/remove crawl-queue {:url url}))

(defn get-crawl-queue []
  (with-collection crawl-queue
    (find {})
    (sort (sorted-map :date 1))))

(defn clear-crawl-queue []
  (mc/remove crawl-queue))

(defn add-crawl-record [record]
  (mc/insert crawl-records record))

(defn get-newest-crawl-records
  ([] (get-newest-crawl-records 20))
  ([lim]
    (with-collection crawl-records
      (find {})
      (sort (sorted-map :date -1))
      (limit lim))))

(defn get-crawl-records-with-errors
  ([] (get-crawl-records-with-errors 100))
  ([lim]
    (with-collection crawl-records
      (find {:errorsPresent true})
      (sort (sorted-map :date -1))
      (limit lim))))


(defn get-stats-by-date-range [start end]
  (with-collection url-stats
    (find {:date {$gte start $lt end}})
    (sort {:date 1})))

(defn get-all-stats 
  ([] (get-all-stats 200))
  ([lim]
   (with-collection url-stats
     (find {})
     (sort {:date -1})
     (limit lim))))

(defn get-stats-for-url 
  ([url] (get-stats-for-url url 100))
  ([url lim]
   (with-collection url-stats
     (find {:url url})
     (sort {:date -1})
     (limit lim))))
