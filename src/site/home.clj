(ns site.home
  (:require [coast]
            [components :refer [subscribe-to-newsletter]]))

(defn index [request]
  [:div.bg-green-cw
   [:header.bg-green-cw.sans-serif
    [:div.mw9.center.pa4.pt5-ns.ph7-l
     [:h1.f2.f1-m.f-headline-l.measure-narrow.lh-title.mv0
      [:span.lh-copy.pa1.tracked-light.white-cw
       "Welcome to "
       [:span.mono.ttu.bg-black-cw.white-cw.ph2
        "Clojure/Walk"]]]
     [:h2.f5.f4-m.f3-l.fw1.i
      "Take a walk to the Clojure and ClojureScript libraries, one at a time."]]]

   [:section.bg-white-cw.black-cw
    [:div.mw9.center.pa4.pt5-ns.ph7-l

     [:h2
      "Check out the latest "
      [:a.link.dim.black-cw.underline
       {:href (coast/url-for :site.video/index)}
       "videos"]
      ":"]
     (for [{:video/keys [title youtubeid] :as video} (coast/q '[:select *
                                                                :from video
                                                                :order id desc
                                                                :limit 3])
           :let                                      [link (str "https://i.ytimg.com/vi/" youtubeid "/sddefault.jpg")]]
       [:div.cf.mv5
        [:img.fl.w-100.w-50-ns {:alt (str "The thumbnail of " title)
                                :src link}]
        [:h2.fl.w-100.w-50-ns.ph3-ns
         [:a.link.dim.black-cw
          {:href (coast/url-for :site.video/player video)}
          title]]])
     [:p.f3.tc
      [:a.link.dim.black-cw.underline
       {:href (coast/url-for :site.video/index)}
       "Show more"]]]]
   [:section
    [:div.mw9.center.pa4.pv5-ns.ph7-l
     (subscribe-to-newsletter)]]])
