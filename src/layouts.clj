(ns layouts
  (:require coast
            components
            [hiccup.page :refer [doctype]]))

(def google-analytics
  "Sorry I need to understand my traffic."
  "<script async src=\"https://www.googletagmanager.com/gtag/js?id=UA-96109475-4\"></script><script>window.dataLayer = window.dataLayer || [];function gtag(){dataLayer.push(arguments);}gtag('js', new Date());gtag('config', 'UA-96109475-4');</script>")

(defn layout [request body & scripts]
  (list
   (doctype :html5)
   [:html {:lang "en"}
    [:head
     [:title "Clojure/Walk"]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:name "description" :content "Clojure tutorials and resources."}]
     [:meta {:name "twitter:card" :content "summary"}]
     [:meta {:name "twitter:creator" :content "@dawranliou"}]
     [:meta {:property "og:url" :content "https://clojurewalk.com"}]
     [:meta {:property "og:title" :content "Clojure/Walk"}]
     [:meta {:property "og:description" :content "Clojure tutorials and resources."}]
     [:meta {:property "og:image" :content "https://clojurewalk.com/assets/img/ClojureWalkLogoSquareBlack-279x279.png"}]
     [:meta {:property "og:image:type" :content "image/png"}]
     [:meta {:property "og:image:width" :content "279"}]
     [:meta {:property "og:image:height" :content "279"}]
     [:link {:rel "apple-touch-icon" :sizes= "57x57" :href "/apple-icon-57x57.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "60x60" :href "/apple-icon-60x60.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "72x72" :href "/apple-icon-72x72.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "76x76" :href "/apple-icon-76x76.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "114x114" :href "/apple-icon-114x114.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "120x120" :href "/apple-icon-120x120.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "144x144" :href "/apple-icon-144x144.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "152x152" :href "/apple-icon-152x152.png"}]
     [:link {:rel "apple-touch-icon" :sizes= "180x180" :href "/apple-icon-180x180.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "192x192" :href "/android-icon-192x192.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon-32x32.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "96x96" :href "/favicon-96x96.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon-16x16.png"}]
     [:link {:rel "manifest" :href "/manifest.json"}]
     [:meta {:name "msapplication-TileColor" :content "#ffffff"}]
     [:meta {:name "msapplication-TileImage" :content "/ms-icon-144x144.png"}]
     [:meta {:name "theme-color" :content "#ffffff"}]
     (coast/raw google-analytics)
     (coast/css "bundle.css")
     (coast/js "bundle.js")
     [:body.bg-black-cw.sans-serif
      (components/navbar)
      [:main.pt5.bg-white-cw
       body]
      (components/footer)
      scripts]]]))

(defn doc-layout [request body]
  (layout request body
          [:script
           "hljs.initHighlightingOnLoad();"]))
