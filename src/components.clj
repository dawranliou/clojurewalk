(ns components
  (:require [coast]))

(defn navbar
  []
  [:nav.w-100.tc.white.fixed.bg-black-cw.z-3.shadow-5
   [:ul.overflow-hidden.menu.db-l.w-100.list.tc.pl0.pt3.mv0.f3.fw3.f5-l

    [:li.absolute.top-1.static-l.ph4.mh2.fw3.di-l.pt1.pb3.pv3-l
     [:a.white.link.dim.mono
      {:href (coast/url-for :site.home/index)}
      [:img.dib.w1.h1.mr1
       {:alt "Site logo"
        :src "/assets/img/ClojureWalkLogoSquare.png"}]
      "Clojure/Walk"]]

    #_[:li.ph4.di-l.pv2.tl.pv0-l]]])

(defn footer
  []
  [:footer.pv4.ph3.ph5-m.ph6-l.mid-gray
   [:small.f6.db.tc
    "© 2019 "
    [:b.ttu
     "Daw-Ran Liou"]
    "., All Rights Reserved"
    [:div.tc.mt3]]])

(defn link-to [url & body]
  [:a {:href url :class "f6 link underline blue"}
   body])

(defn button-to
  ([am m s]
   (let [data (select-keys m [:data-confirm])
         form (select-keys am [:action :_method :method :class])]
     (coast/form (merge {:class "dib ma0"} form)
                 [:input (merge data {:class "input-reset pointer link underline bn f6 br2 ma0 pa0 dib blue bg-transparent"
                                      :type  "submit"
                                      :value s})])))
  ([am s]
   (button-to am {} s)))

(defn container [m & body]
  (let [mw (or (:mw m) 8)]
    [:div {:class (str "pa4 w-100 center mw" mw)}
     [:div {:class "overflow-auto"}
      body]]))

(defn table [& body]
  [:table {:class "f6 w-100" :cellspacing 0}
   body])

(defn thead [& body]
  [:thead body])

(defn tbody [& body]
  [:tbody {:class "lh-copy"} body])

(defn tr [& body]
  [:tr {:class "stripe-dark"}
   body])

(defn th
  ([s]
   [:th {:class "fw6 tl pa3 bg-white"} s])
  ([]
   (th "")))

(defn td [& body]
  [:td {:class "pa3"} body])

(defn submit [value]
  [:input {:class "input-reset pointer dim ml3 db bn f6 br2 ph3 pv2 dib white bg-blue"
           :type  "submit"
           :value value}])

(defn dt [s]
  [:dt {:class "f6 b mt2"} s])

(defn dd [s]
  [:dd {:class "ml0"} s])

(defn dl [& body]
  [:dl body])

(defn form-for
  ([k body]
   (form-for k {} body))
  ([k m body]
   (form-for k m {} body))
  ([k m params body]
   (coast/form-for k m (merge params {:class "pa4"})
                   [:div {:class "measure"}
                    body])))

(defn label [m s]
  [:label (merge {:for s :class "f6 b db mb2"} m) s])

(defn input [m]
  [:input (merge {:class "input-reset ba b--black-20 pa2 mb2 db w-100 outline-0"} m)])

(defn text-muted [s]
  [:div {:class "f6 tc gray"}
   s])

(defn el [k m]
  (fn [& body]
    [k m body]))

(->> [:mr1 :mr2 :mr3 :mr4 :mr5]
     (mapv name)
     (mapv #(coast/intern-var % (el :span {:class %}))))

(defn tc [& body]
  [:div {:class "tc"}
   body])

(defn youtube-player
  [youtubeid]
  [:div.mv4.video-container
   [:iframe
    {:width           560
     :height          315
     :src             (str "https://www.youtube.com/embed/" youtubeid)
     :frameborder     "0"
     :allow           "accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
     :allowfullscreen nil}]])
