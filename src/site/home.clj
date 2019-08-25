(ns site.home
  (:require [coast]))


(defn index [request]
  [:div.pa4.w-100.center.mw8
   [:h1 {:class "tc"}
    "Clojure/Walk"]
   [:p.tc "Coming soon!"]])
