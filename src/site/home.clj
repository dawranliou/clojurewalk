(ns site.home
  (:require [coast]))


(defn index [request]
  [:h1 {:class "tc"}
   "Clojure/Walk"])
