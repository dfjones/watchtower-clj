(ns watchtower-clj.stats
  (:use watchtower-clj.db)
  (:use clj-time.core)
  (:refer-clojure :exclude [extend])
  (:require [clojure.math.numeric-tower :as math]))


(defn update-stats [crawl-record]
  (let [r {
           :url (:url crawl-record)
           :renderTime (:renderTime crawl-record)
           :serverErrors (count (:serverErrors crawl-record))
           :browserErrors (count (:browserErrors crawl-record))
           :date (:date crawl-record)}]
    (store-url-stat r)))

(defn current-hour [date]
  (date-time (year date) (month date) (day date) (hour date)))

(defn most-recent-slice-end [time-slice date]
  (let [nh (current-hour date)
        incr (fn incr [dt]
               (let [nt (plus dt time-slice)]
                 (if (or (= nt date)(before? nt date))
                   (incr nt)
                   dt)))]
    (incr nh)))


(defn gen-date-ranges [time-slice num-slices]
  (let [gen-next-slice #(minus %1 time-slice)
        curr-time (now)
        gen (fn gen [curr-time i l]
              (let [next-time (gen-next-slice curr-time)
                    nl (cons (list next-time curr-time) l)]
                (if (< i (dec num-slices))
                  (gen next-time (inc i) nl)
                  nl))) 
        last-slice (most-recent-slice-end time-slice curr-time)]
    (gen last-slice 1 [(list last-slice curr-time)])))


(defn zero-or-max [n]
  (if (empty? n)
    0
    (max n)))

(defn avg [n]
  (if (empty? n)
    0
    (/ (reduce + n) (count n))))

(defn percentile [n percent]
  (if (or (nil? n) (empty? n))
    0
    (let [sn (sort n)
          k (* (- (count sn) 1) percent)
          f (math/floor k)
          c (math/ceil k)]
      (if (= f c)
        (nth sn k)
        (let [d0 (* (nth sn f) (- c k))
              d1 (* (nth sn c) (- k f))]
          (+ d0 d1))))))

(defn gen-stats [raw dkey]
  (let [d (for [r raw] (dkey r))]
    {:count (count d)
     :max (zero-or-max d)
     :median (percentile d 0.5)
     :p95 (percentile d 0.95)
     :p85 (percentile d 0.85)
     :p75 (percentile d 0.75)
     :avg (avg d)}))

(defn gen-stats-package [raw]
  {:renderTime (gen-stats raw :renderTime)
   :serverErrors (gen-stats raw :serverErrors)
   :browserErrors (gen-stats raw :browserErrors)})

(defn gen-stats-summary [time-slice num-slices]
  (let [date-ranges (gen-date-ranges time-slice num-slices)]
    (for [r date-ranges] 
      (gen-stats-package (get-stats-by-date-range (first r) (second r))))))

 
