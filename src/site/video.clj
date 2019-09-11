(ns site.video
  (:require coast
            [components :refer [container]]))

(defn card
  [title link {:keys [background-image]}]
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
  [_]
  (let [serieses (coast/q '[:pull series/title series/slug {:series/videos [video/title video/youtubeid video/slug]} :from series])]
    (container
     {:mw 8}
     (for [{:series/keys [title videos] :as series} serieses]
       [:div.cf.mv3
        [:h2.f3.mono.bg-green-cw.pa4 (str "> " title " (" (count videos) " episodes)")]
        [:div.flex.flex-column.flex-row-l.justify-between
         (for [{:video/keys [title youtubeid] :as video} (take 2 videos)]
           (card title
                 (coast/url-for :site.video/player video)
                 {:background-image (str "background-image: url(https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg)")}))
         (card "Watch series >>" (coast/url-for ::series series) {})]]))))

(defn series
  [request]
  (let [series-slug (-> request :params :series-slug)
        videos      (coast/q '[:select video/title video/youtubeid video/slug series/title
                               :from video
                               :join [series video/series series/id]
                               :where [:series/slug ?series/slug]]
                             {:series/slug series-slug})
        series      (-> videos first :series/title)]
    (container
     {:mw 8}
     (if (some? videos)
       [:div.cf.mv3
        [:h2.f3.mono.bg-green-cw.pa4 (str "> " series)]
        (for [row (partition 3 3 (repeat nil) videos)]
          [:div.flex.flex-column.flex-row-l.justify-between
           (for [{:video/keys [title youtubeid] :as video} row]
             (if video
               (card title
                     (coast/url-for :site.video/player video)
                     {:background-image (str "background-image: url(https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg)")})
               [:div.fl.w-100.h5.w-30-l.dt
                [:p.dtc.v-mid.tc "🎉🎉🎉"]]))])]
       [:h1 "Sorry we cannot find your series."]))))

(defn player
  [{:keys [params] :as _}]
  (let [video-slug  (:video-slug params)
        row         (coast/pluck
                     '[:select * :from video :where [slug ?video-slug]]
                     {:video-slug video-slug})
        youtubeid   (:video/youtubeid row)
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
