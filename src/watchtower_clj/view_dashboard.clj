(ns watchtower-clj.view-dashboard
  (:use watchtower-clj.view-base)
  (:use [hiccup core]))



(defn view-dashboard [context]
  (base {
          :title "Dashboard"
          :content
            (html
              [:div.row
                [:div.span5
                  [:h1 "p95 Render Time"]
                  [:div#p95RenderTime]]
                [:div.span5
                  [:h1 "Avg Render Time"]
                  [:div#avgRenderTime]]
                ]
              [:div.row
                [:div.span5
                  [:h1 "Total JS Errors"]
                  [:div#totalJSErrors]]
                [:div.span5
                  [:h1 "Total Server Errors"]
                  [:div#totalServerErrors]]
              ]
            )
          :footer
            (html
              [:script {:src "/js/jquery.sparkline.js"}]
              [:script {:src "/js/dashboard-graph.js"}]
            )
        }
  )
)