(ns site.home
  (:require [coast]))

(defn index [request]
  [:div

   [:nav.w-100.tc.white.fixed.bg-dark-clojurewalk.z-3.shadow-5
    [:ul.overflow-hidden.menu.db-l.w-100.list.tc.pl0.pt3.mv0.f3.fw3.f5-l

     [:li.absolute.top-1.static-l.ph4.mh2.fw3.di-l.pt1.pb3.pv3-l
      [:a.white.link.dim.mono
       {:href (coast/url-for :site.home/index)}
       [:img.dib.w1.h1.mr1
        {:alt "Site logo"
         :src "/assets/img/ClojureWalkLogoSquare.png"}]
       "Clojure/Walk"]]

     #_[:li.ph4.di-l.pv2.tl.pv0-l]]]
   [:main.pt5
    [:div.pa4.w-100.center.mw8
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
          :allowfullscreen nil}]])]]])
