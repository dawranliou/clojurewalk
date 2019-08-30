(ns site.video
  (:require [components]
            [coast]))

(defn index
  [request]
  (let [youtubeid   (-> request :params :youtubeid)
        row         (coast/pluck
                      '[:select * :from video :where [youtubeid ?youtubeid]]
                      {:youtubeid youtubeid})
        title       (:video/title row)
        description (:video/description row)]
    [:div
     [:main.pt5.bg-white-cw
      [:div.pa4.w-100.center.mw8
       [:div.tc
        (if (some? row)
          [:div
           (components/youtube-player youtubeid)
           [:h2 title]
           (if description
             [:p description]
             [:p.i.gray "This video has no description."])]
          [:h1 "Sorry we cannot find your video."])]]]]))

(comment
  (def youtubeid "bPz4-Vcx27A")
  (coast/pluck
    '[:select * :from video :where [youtubeid ?youtubeid]]
    {:youtubeid youtubeid}))
