(ns site.video
  (:require [components]
            [coast]))

(defn index
  [request]
  [:div.pa4.w-100.center.mw8
   (for [{:video/keys [title youtubeid]}
         (coast/q '[:select *
                    :from video
                    :order id
                    :limit 10])

         :let [link (str "https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg")]]
     [:div.tc.mv5
      [:h2
       [:a.link.dim.black-cw
        {:href (coast/url-for :site.video/player {:youtubeid youtubeid})}
        title]]
      [:img {:alt (str "The thumbnail of " title)
             :src link}]])])

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
    {:youtubeid youtubeid}))
