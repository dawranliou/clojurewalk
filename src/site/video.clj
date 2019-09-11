(ns site.video
  (:require [components :refer [container]]
            [coast]))

(defn card
  [title link {:keys [background-image] :as opts}]
  [:div.fl.w-100.h5.w-30-l.cover.bg-center.dt.ba.br2.mb3
   (when background-image {:class "hide-child" :style background-image})
   [:a.link.dtc.v-mid.w-100.h-100.pa5.tc
    (if background-image
      {:href  link
       :class "child bg-black-90 white"}
      {:href  link
       :class "black dim"})
    title]])

(defn index
  [request]
  (let [serieses (coast/q '[:pull series/title {:series/videos [video/title video/youtubeid]} :from series])]
    (container
     {:mw 8}
     (for [{:series/keys [title videos]} serieses]
       [:div.cf.mv3
        [:h2.f3.mono.bg-green-cw.pa4 (str "> " title)]
        [:div.flex.flex-column.flex-row-l.justify-between
         (for [{:video/keys [title youtubeid]} (take 2 videos)]
           (card title
                 (coast/url-for :site.video/player {:youtubeid youtubeid})
                 {:background-image (str "background-image: url(https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg)")}))
         (card "Watch series >>" "#" {})]]))))

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
