(ns site.home
  (:require [coast]))

(defn index [request]
  [:div
   [:header.bg-green-cw.sans-serif
    [:div.mw9.center.pa4.pt5-ns.ph7-l
     [:h1.f2.f1-m.f-headline-l.measure-narrow.lh-title.mv0
      [:span.lh-copy.pa1.tracked-light.white-cw
       "Welcome to "
       [:span.mono.ttu.bg-black-cw.gold.ph2
        "Clojure/Walk"]]]
     [:h2.f3.fw1.i
      "Take a walk to the Clojure and ClojureScript libraries, one at a time."]]]
   [:section.bg-white-cw.black-cw
    [:div.mw9.center.pa4.pt5-ns.ph7-l
     [:h2
      "Check out the "
      [:a.link.dim.green-cw.underline
       {:href (coast/url-for :site.video/index)}
       "videos"]
      "!"]]]])
