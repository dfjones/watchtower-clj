(ns watchtower-clj.stats
  (:use watchtower-clj.db)
  (:use clj-time.core))


(defn update-stats [crawl-record]
  (let [r {
           :url (:url crawl-record)
           :renderTime (:renderTime crawl-record)
           :serverErrors (count (:serverErrors crawl-record))
           :browserErrors (count (:browserErrors crawl-record))
           :date (:date crawl-record)}]
    (store-url-stat r)))

(defn get-stats-summary [time-slice, num-slices]
  )


(defn current-hour [date]
  (date-time (year date) (month date) (day date) (hour date)))

(defn most-recent-slice-end [time-slice, date]
  (let [nh (current-hour date)
        incr (fn incr [dt]
               (let [nt (plus dt time-slice)]
                 (if (or (= nt date)(before? nt date))
                   (incr nt)
                   dt)))]
    (incr nh)))


(defn gen-date-ranges [time-slice, num-slices]
  )