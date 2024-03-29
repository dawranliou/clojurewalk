(ns series
  (:require coast
            [components :refer [container dd dt input label link-to submit]]))

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
                             (label {:for "series/slug"} "slug")
                             (input {:type "text" :name "series/slug" :value (-> request :params :series/slug)})

                             (label {:for "series/title"} "title")
                             (input {:type "text" :name "series/title" :value (-> request :params :series/title)})

                             (link-to (coast/url-for :admin/dashboard) "Cancel")
                             (submit "New series"))))

(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:series/slug :series/title]]])
                       (select-keys [:series/slug :series/title])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (build (merge request errors)))))

(defn edit [request]
  (let [series (coast/fetch :series (-> request :params :series-id))]
    (container {:mw 6}
               (when (some? (:errors request))
                 (errors (:errors request)))

               (coast/form-for ::change series
                               (label {:for "series/slug"} "slug")
                               (input {:type "text" :name "series/slug" :value (:series/slug series)})

                               (label {:for "series/title"} "title")
                               (input {:type "text" :name "series/title" :value (:series/title series)})

                               (link-to (coast/url-for :admin/dashboard) "Cancel")
                               (submit "Update series")))))

(defn change [request]
  (let [series     (coast/fetch :series (-> request :params :series-id))
        [_ errors] (-> (select-keys series [:series/id])
                       (merge (:params request))
                       (coast/validate [[:required [:series/id :series/slug :series/title]]])
                       (select-keys [:series/id :series/slug :series/title])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (edit (merge request errors)))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :series (-> request :params :series-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (-> (coast/redirect-to :admin/dashboard)
          (coast/flash "Something went wrong!")))))
