(ns site.about
  (:require [components :refer [container]]))

(defn index
  [request]
  (container
    {:mw 6}
    [:article.measure
     [:h1.tc "About "
      [:span.mono.bg-green-cw.ttu.fw4 "Clojure/Walk"]]
     [:p.lh-copy
      "We want to help beginner-to-intermediate fellow Clojurists to improve your skills!
Clojure is powerful. However, in order to be effective in Clojure there are many alient
things most of us need to figure out ourself, especially when there's no one there to 'show'
you. Clojure/Walk is a place to learn Clojure and Clojure/Script libraries. We want to curate
video contents and articles for beginners."]]))
