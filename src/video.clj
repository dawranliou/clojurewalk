(ns video
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))

(defn index [request]
  (let [rows (coast/q '[:select *
                        :from video
                        :order id
                        :limit 10])]
    (container {:mw 8}
               (when (not (empty? rows))
                 (link-to (coast/url-for ::build) "New video"))

               (when (empty? rows)
                 (tc
                  (link-to (coast/url-for ::build) "New video")))

               (when (not (empty? rows))
                 (table
                  (thead
                   (tr
                    (th "youtubeid")
                    (th "id")
                    (th "updated-at")
                    (th "created-at")
                    (th "title")
                    (th "series")
                    (th "description")
                    (th "")
                    (th "")
                    (th "")))
                  (tbody
                   (for [row rows]
                     (tr
                      (td (:video/youtubeid row))
                      (td (:video/id row))
                      (td (:video/updated-at row))
                      (td (:video/created-at row))
                      (td (:video/title row))
                      (td (:video/series row))
                      (td (:video/description row))
                      (td
                       (link-to (coast/url-for ::view row) "View"))
                      (td
                       (link-to (coast/url-for ::edit row) "Edit"))
                      (td
                       (button-to (coast/action-for ::delete row) {:data-confirm "Are you sure?"} "Delete"))))))))))

(defn view [request]
  (let [id    (-> request :params :video-id)
        video (coast/fetch :video id)]
    (container {:mw 8}
               (dl
                (dt "youtubeid")
                (dd (:video/youtubeid video))

                (dt "title")
                (dd (:video/title video))

                (dt "series")
                (dd (:video/series video))

                (dt "description")
                (dd (:video/description video)))
               (mr2
                (link-to (coast/url-for ::index) "List"))
               (mr2
                (link-to (coast/url-for ::edit {::id id}) "Edit"))
               (mr2
                (button-to (coast/action-for ::delete {::id id}) {:data-confirm "Are you sure?"} "Delete")))))

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

                             (link-to (coast/url-for ::index) "Cancel")
                             (submit "New video"))))

(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:video/youtubeid :video/title :video/series :video/description]]])
                       (select-keys [:video/youtubeid :video/title :video/series :video/description])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
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

                               (link-to (coast/url-for ::index) "Cancel")
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
      (coast/redirect-to ::index)
      (edit (merge request errors)))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :video (-> request :params :video-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
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
