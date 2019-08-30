(ns site.video
  (:require [components]
            [coast]))

(defn index
  [request]
  (let [youtubeid (-> request :params :youtubeid)
        row       (coast/pluck
                    '[:select * :from video :where [youtubeid ?youtubeid]]
                    {:youtubeid youtubeid})]
    [:div
     [:main.pt5.bg-white-cw
      [:div.pa4.w-100.center.mw8
       [:div.tc
        (if (some? row)
          (components/youtube-player youtubeid)
          [:h1 "Sorry we cannot find your video."])]]]]))

(comment
  (def youtubeid "bPz4-Vcx27A"))
