(ns watchtower-clj.view-base
  (:use [hiccup core page]))

(defn base [{title :title content :content footer :footer}]
  (let [nav-active #(if (= title %1) {:class "active"} {})]
    (html5
      [:head
        [:meta {:charset "utf-8"} ]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name "description" :content ""}]
        [:meta {:name "author" :content "Doug Jones"}]
        [:title title]
        [:link {:href "/css/bootstrap.css" :rel "stylesheet"}]
        [:style "body { padding-top: 60px; }"]
        [:link {:href "/css/bootstrap-responsive.css" :rel "stylesheet"}]]
      [:body
        [:div.navbar.navbar-inverse.navbar-fixed-top
          [:div.navbar-inner
            [:div.container
              [:a.btn.btn-navbar {:data-toggle "collapse" :data-target ".nav-collapse"}
                [:span.icon-bar]
                [:span.icon-bar]
                [:span.icon-bar]
              ]
              [:a {:class "brand" :href "/"} "Watchtower"]
              [:div.nav-collapse.collapse
                [:ul.nav
                  [:li (nav-active "Dashboard") [:a {:href "/"} "Dashboard"]]
                  [:li (nav-active "Crawl Log") [:a {:href "/crawlog"} "Crawl Log"]]
                  [:li (nav-active "About") [:a {:href "/about"} "About"]]
                ]
              ]
            ]
          ]
        ]
        [:div.container content]
        [:script {:src "/js/jquery.js"}]
        [:script {:src "/js/bootstrap.js"}]
        footer
      ]
  )))