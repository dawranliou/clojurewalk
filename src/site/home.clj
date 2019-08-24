(ns site.home
  (:require [coast]))


(defn index [request]
  [:div
   [:h1 {:class "tc"}
    "Clojure/Walk"]
   [:p.tc "Coming soon!"]])
