(ns video
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))

(defn errors [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (dt (str k))
       (dd v)])]])

(defn build [request]
  (container {:mw 6}
             (when (some? (:errors request))
               (errors (:errors request)))

             (coast/form-for ::create
                             (label {:for "video/youtubeid"} "youtubeid")
                             (input {:type "text" :name "video/youtubeid" :value (-> request :params :video/youtubeid)})

                             (label {:for "video/title"} "title")
                             (input {:type "text" :name "video/title" :value (-> request :params :video/title)})

                             (label {:for "video/series"} "series")
                             (input {:type "text" :name "video/series" :value (-> request :params :video/series)})

                             (label {:for "video/description"} "description")
                             (input {:type "text" :name "video/description" :value (-> request :params :video/description)})

                             (link-to (coast/url-for :admin/dashboard) "Cancel")
                             (submit "New video"))))

(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:video/youtubeid :video/title :video/series :video/description]]])
                       (select-keys [:video/youtubeid :video/title :video/series :video/description])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (build (merge request errors)))))

(defn edit [request]
  (let [video (coast/fetch :video (-> request :params :video-id))]
    (container {:mw 6}
               (when (some? (:errors request))
                 (errors (:errors request)))

               (coast/form-for ::change video
                               (label {:for "video/youtubeid"} "youtubeid")
                               (input {:type "text" :name "video/youtubeid" :value (:video/youtubeid video)})

                               (label {:for "video/title"} "title")
                               (input {:type "text" :name "video/title" :value (:video/title video)})

                               (label {:for "video/series"} "series")
                               (input {:type "text" :name "video/series" :value (:video/series video)})

                               (label {:for "video/description"} "description")
                               (input {:type "text" :name "video/description" :value (:video/description video)})

                               (link-to (coast/url-for :admin/dashboard) "Cancel")
                               (submit "Update video")))))

(defn change [request]
  (let [video      (coast/fetch :video (-> request :params :video-id))
        [_ errors] (-> (select-keys video [:video/id])
                       (merge (:params request))
                       (coast/validate [[:required [:video/id :video/youtubeid :video/title :video/series :video/description]]])
                       (select-keys [:video/id :video/youtubeid :video/title :video/series :video/description])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (edit (merge request errors)))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :video (-> request :params :video-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (-> (coast/redirect-to :admin/dashboard)
          (coast/flash "Something went wrong!")))))

(comment
  (coast/q '[:select * :from video])

  (coast/delete {:video/id 1})

  (coast/insert [#:video{:youtubeid "bPz4-Vcx27A"
                         :title     "Web Development with Coast part 1 - Quickstart"}
                 #:video{:youtubeid "KY15lX2xaic"
                         :title     "Web Development with Coast part 2 - Deploy"}
                 #:video{:youtubeid "nb92CTLYTtk"
                         :title     "Web development with Coast and Clojure part 3 - Deirectory Structure"}
                 #:video{:youtubeid "B63pQQZhFBA"
                         :title     "Web Development with Coast part 4 - View and Hiccup"}
                 #:video{:youtubeid "Wm2wQILm0x4"
                         :title     "Web Development with Coast part 5 - Authentication Middleware"}]))
