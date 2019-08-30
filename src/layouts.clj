(ns layouts
  (:require [hiccup.page :refer [doctype]]
            [components]))

(defn layout [request body]
  (list
    (doctype :html5)
    [:html
     [:head
      [:title "Clojure/Walk"]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:meta {:name "description" :content "Clojure tutorials and resources."}]
      [:meta {:name "twitter:card" :content "summary"}]
      [:meta {:name "twitter:creator" :content "@dawranliou"}]
      [:meta {:property "og:url" :content "https://clojurewalk.com"}]
      [:meta {:property "og:title" :content "Clojure/Walk"}]
      [:meta {:property "og:description" :content "Clojure tutorials and resources."}]
      [:meta {:property "og:image" :content "https://clojurewalk.com/assets/img/ClojureWalkLogoSquareBlack.png"}]
      [:meta {:property "og:image:type" :content "image/png"}]
      [:meta {:property "og:image:width" :content "720"}]
      [:meta {:property "og:image:height" :content "720"}]
      [:link {:rel= "short icon" :type "image/x-icon" :href "/favicon.ico"}]
      [:link {:rel= "icon" :type "image/x-icon" :href "/favicon.ico"}]
      [:link {:href "https://fonts.googleapis.com/css?family=IBM+Plex+Mono:300&display=swap" :rel "stylesheet"}]
      (coast/css "bundle.css")
      (coast/js "bundle.js")
      [:body.bg-black-cw
       (components/navbar)
       body
       (components/footer)]]]))
