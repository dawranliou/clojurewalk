(ns site.home
  (:require [coast]))

(defn index [request]
  [:div.pa4.w-100.center.mw8
   [:h1 {:class "tc"}
    "Clojure/Walk"]
   (for [{:video/keys [title youtubeid]}
         (coast/q '[:select *
                    :from video
                    :order id
                    :limit 10])

         :let [link (str "https://www.youtube-nocookie.com/embed/" youtubeid)]]
     [:div.tc.mv5
      [:h2 title]
      [:iframe
       {:width           560
        :height          315
        :src             link
        :frameborder     "0"
        :allow           "accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
        :allowfullscreen nil}]])])
