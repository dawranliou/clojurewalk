(ns post
  (:require coast
            [components :refer [container dd dt input submit-block textarea]]
            [helpers :as h]
            [markdown.core :as markdown]))

(defn errors [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (dt (str k))
       (dd v)])]])

(defn preview [request]
  (let [{:keys [title body]} (:params request)]
    (coast/ok
     [:article
      [:h1.ma0 title]
      (coast/raw (markdown/md-to-html-string body))]
     :html)))

(defn form [form-params request]
  [:div
   (coast/form
    form-params
    (when (some? (:errors request))
      (errors (:errors request)))

    (input {:type "text" :placeholder "Title" :name "post/title" :value (-> request :params :post/title)})

    [:div.mb3]
    (textarea {:placeholder "Body" :name "post/body"}
              (-> request :params :post/body))

    [:div.mb3]
    (submit-block "Publish"))])

(defn build [request]
  (container
   {:mw 7}
   [:div.cf
    [:div.fl
     [:div.f6.gray.mb2
      {:id "status"}
      (coast/raw "Unsaved")]]
    [:div.fr
     [:a.blue.pointer.mr3 {:id "edit"} "Edit"]
     [:a.blue.pointer {:id "preview"} "Preview"]]]
   [:div {:id "preview-container"}]
   [:div {:id "form-container"}
    (form (coast/action-for ::create) request)]))

(defn create [{:keys [member params] :as request}]
  (let [xhr?          (coast/xhr? request)
        params        (if xhr?
                        params
                        (assoc params :post/published-at (coast/now)))
        params        (assoc params :post/slug (h/slug (:post/title (:params request))))
        [post errors] (-> (merge params {:post/member (:member/id member)})
                          (coast/validate [[:required [:post/member]]])
                          (select-keys [:post/member :post/body :post/published-at :post/slug :post/title])
                          (coast/insert)
                          (coast/rescue))]
    (if (nil? errors)
      (if xhr?
        (coast/ok {:form-params (coast/action-for ::change post)
                   :url         (coast/url-for ::edit post)}
                  :json)
        (coast/redirect-to :admin/dashboard))
      (if xhr?
        (coast/server-error (form (coast/action-for ::create) (merge request errors)))
        (build (merge request errors))))))

(defn edit [request]
  (let [post (coast/fetch :post (-> request :params :post-id))]
    (container {:mw 7}
               [:div.cf
                [:div.fl
                 [:div {:id "status" :class "f6 gray mb2"} (coast/raw "Saved")]]
                [:div.fr
                 [:a {:class "blue pointer dim mr3" :id "edit"} "Edit"]
                 [:a {:class "gray pointer dim mr3" :id "preview"} "Preview"]
                 (coast/form (merge (coast/action-for ::change post) {:class "dib ma0"})
                             [:input {:class "input-reset bn bg-transparent gray pointer dim"
                                      :type  "submit"
                                      :name  "submit"
                                      :value "Unpublish"}])]]

               [:div {:id "preview-container"}]
               [:div {:id "form-container"}
                (form (coast/action-for ::change post) {:params post})])))

(defn change [{:keys [params] :as request}]
  (let [post       (coast/fetch :post (:post-id params))
        post       (if (some? (:post-slug post))
                     post
                     (assoc post :post/slug (h/slug (:post/title params))))
        post       (case (:submit params)
                     "Publish"      (if (some? (:post/published-at post))
                                      post
                                      (assoc post :post/published-at (coast/now)))
                     "Un-published" (assoc post :post/published-at nil)
                     post)
        [_ errors] (-> (select-keys post [:post/id :post/member :post/slug :post/published-at])
                       (merge (select-keys params [:post/title :post/body]))
                       (coast/validate [[:required [:post/id :post/member]]])
                       (select-keys [:post/id :post/member :post/body :post/published-at :post/slug :post/title])
                       (coast/update)
                       (coast/rescue))]
    (if (coast/xhr? request)
      (coast/ok {} :json)
      (if (nil? errors)
        (coast/redirect-to :admin/dashboard)
        (edit (merge request errors))))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :post (-> request :params :post-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (-> (coast/redirect-to :admin/dashboard)
          (coast/flash "Something went wrong!")))))
