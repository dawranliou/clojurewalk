(ns site.home
  (:require [coast]
            ))

(defn index [request]
  [:main.pt5.bg-white-cw
   [:div.pa4.w-100.center.mw8
    [:div.tc
     [:p.f1.dib.mono.bg-green-cw
      "Coming soon!"]]
    (for [{:video/keys [title youtubeid]}
          (coast/q '[:select *
                     :from video
                     :order id
                     :limit 10])

          :let [link (str "https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg")]]
      [:div.tc.mv5
       [:h2
        [:a.link.dim.black-cw
         {:href (coast/url-for :site.video/index {:youtubeid youtubeid})}
         title]]
       [:img {:alt (str "The thumbnail of " title)
              :src link}]])]])
