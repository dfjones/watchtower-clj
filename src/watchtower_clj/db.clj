(ns watchtower-clj.db
  (:require [monger.core :as mg])
  (:require [monger.collection :as mc])
  (:refer-clojure :exclude [sort find])
  (:use monger.query)
  (:import [com.mongodb MongoOptions ServerAddress])
  (:import [java.util Date]))

(mg/connect!)

(mg/set-db! (mg/get-db "Watchtower"))

(def crawl-queue "crawl_queue")
(def crawl-records "crawl_records")

(defn in-crawl-queue [url]
  (> (mc/count
       (mc/find crawl-queue {:url url}))
    0))

(defn add-to-crawl-queue
  ([url] (add-to-crawl-queue url (new Date)))
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
  ([limit]
    (with-collection crawl-records
      (find {:errorsPresent true})
      (sort (sorted-map :date -1))
      (limit limit))))

