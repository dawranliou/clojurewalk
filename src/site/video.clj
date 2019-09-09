(ns site.video
  (:require [components :refer [container]]
            [coast]))

(defn index
  [request]
  (let [serieses (coast/q '[:pull series/title {:series/videos [video/title video/youtubeid]} :from series])]
    (container
      {:mw 8}
      (for [{:series/keys [title videos]} serieses]
        [:div.cf.mv3
         [:h2 title]
         (for [{:video/keys [title youtubeid]} videos
               :let                            [link (str "https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg")]]
           [:div.fl.w-100.w-third-ns.pa2
            [:h2.f6
             [:a.link.dim.black-cw
              {:href (coast/url-for :site.video/player {:youtubeid youtubeid})}
              title]]
            [:img {:alt (str "The thumbnail of " title)
                   :src link}]])]))))

(defn player
  [request]
  (let [youtubeid   (-> request :params :youtubeid)
        row         (coast/pluck
                      '[:select * :from video :where [youtubeid ?youtubeid]]
                      {:youtubeid youtubeid})
        title       (:video/title row)
        description (:video/description row)]
    [:div.pa4.w-100.center.mw8
     [:div.tc
      (if (some? row)
        [:div
         (components/youtube-player youtubeid)
         [:h2 title]
         (if description
           [:p description]
           [:p.i.gray "This video has no description."])]
        [:h1 "Sorry we cannot find your video."])]]))

(comment
  (def youtubeid "bPz4-Vcx27A")
  (coast/pluck
    '[:select * :from video :where [youtubeid ?youtubeid]]
    {:youtubeid youtubeid})
  (coast/q
    '[:select video/youtubeid video/title series/name
      :from video :join [series video/series series/id]])
  (coast/q
    '[:pull *
      :from series])
  (coast/q '[:pull series/name {:series/videos [video/title video/youtubeid]} :from series]))
