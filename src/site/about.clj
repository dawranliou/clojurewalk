(ns site.about
  (:require [coast]
            [components :refer [container subscribe-to-newsletter banner]]))

(def about-content
  ["Hi, fellow beginner Clojurists, I want to help you to know your craft.
When I started learning Clojure, I was desperate to find someone to
show me what I can do with the language.
Clojure is powerful. However, to be effective there are many alien
territories any Clojurist need to force themselves into
(especially when we came to Clojure with the presumptions from our previous programming experiences.)"
   "Clojure/Walk is a place to learn Clojure and ClojureScript libraries.
By showing you the libraries, you will learn about: REPL driven development,
the Clojure ecosystem, and how to be on your own."
   "I hope you enjoy the contents. At this point, this is my side project
and I will post updates as frequently as I could. If you have any suggestion,
you're welcome to contact me."])

(defn index
  [{:keys [errors flash]}]
  (container
    {:mw 6}
    [:article.measure.f5.f4-ns
     (banner errors :type :warning)
     (banner flash :type :info)
     [:h1.tc "About "
      [:span.mono.bg-green-cw.ttu.fw4 "Clojure/Walk"]]
     (for [paragraph about-content]
       [:p.lh-copy paragraph])

     [:p.tr
      [:a.link.dim.black-cw.underline {:href "mailto://dawran6@gmail.com"} "dawran6@gmail.com"]]
     [:p.tr
      [:a.link.dim.black-cw.underline {:href "https://twitter.com/dawranliou"} "@dawranliou"]]
     [:p.tr
      "~ Daw-Ran Liou 2019/9/19"]]
    (subscribe-to-newsletter)))

(defn subscribe
  [request]
  (let [[_ errors] (-> (:params request)
                       (select-keys [:maillist/email])
                       (coast/validate [[:email [:maillist/email]]
                                        [:required [:maillist/email]]])
                       (coast/insert)
                       (coast/rescue))]
    (if (some? errors)
      (index (merge request errors))
      (-> (coast/redirect-to ::index)
          (coast/flash "Thank you for subscribing!")))))

#_(defn unsubscribe
    [request])
