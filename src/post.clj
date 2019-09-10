(ns post
  (:require [coast]
            [markdown.core :as markdown]
            [components :refer [submit-block textarea container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))

(defn index [request]
  (let [rows (coast/q '[:select *
                        :from post
                        :order id
                        :limit 10])]
    (container {:mw 8}
               (when (not (empty? rows))
                 (link-to (coast/url-for ::build) "New post"))

               (when (empty? rows)
                 (tc
                   (link-to (coast/url-for ::build) "New post")))

               (when (not (empty? rows))
                 (table
                   (thead
                     (tr
                       (th "member")
                       (th "body")
                       (th "id")
                       (th "published-at")
                       (th "updated-at")
                       (th "slug")
                       (th "created-at")
                       (th "title")
                       (th "")
                       (th "")
                       (th "")))
                   (tbody
                     (for [row rows]
                       (tr
                         (td (:post/member row))
                         (td (:post/body row))
                         (td (:post/id row))
                         (td (:post/published-at row))
                         (td (:post/updated-at row))
                         (td (:post/slug row))
                         (td (:post/created-at row))
                         (td (:post/title row))
                         (td
                           (link-to (coast/url-for ::view row) "View"))
                         (td
                           (link-to (coast/url-for ::edit row) "Edit"))
                         (td
                           (button-to (coast/action-for ::delete row) {:data-confirm "Are you sure?"} "Delete"))))))))))

(defn view [request]
  (let [id   (-> request :params :post-id)
        post (coast/fetch :post id)]
    (container {:mw 8}
               (dl
                 (dt "member")
                 (dd (:post/member post))

                 (dt "body")
                 (dd (:post/body post))

                 (dt "published-at")
                 (dd (:post/published-at post))

                 (dt "slug")
                 (dd (:post/slug post))

                 (dt "title")
                 (dd (:post/title post)))
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

(defn preview [request]
  (let [{:keys [title body]} (:params request)]
    (coast/ok
      [:div
       [:h2.f2.f1-m.f-subheadline-l title]
       [:div.content
        (coast/raw (markdown/md-to-html-string body))]]
      :html)))

(defn form [form-params request]
  [:div
   (coast/form
     form-params
     (when (some? (:errors request))
       (errors (:errors request)))

     (input {:type "text" :placeholder "Title" :name "post/title" :value (-> request :params :post/title)})

     [:div.mb3]
     (textarea {:placeholder "Body" :name "post/body" :style "height: calc(100% - 340px)"}
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

    (container
      {:mw 7}
      [:div {:id "preview-container"}])

    [:div {:id "form-container"}
     (form (coast/action-for ::create) request)]))

(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:post/member :post/body :post/published-at :post/slug :post/title]]])
                       (select-keys [:post/member :post/body :post/published-at :post/slug :post/title])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (build (merge request errors)))))

(defn edit [request]
  (let [post (coast/fetch :post (-> request :params :post-id))]
    (container {:mw 6}
               (when (some? (:errors request))
                 (errors (:errors request)))

               (coast/form-for ::change post
                               (label {:for "post/member"} "member")
                               (input {:type "text" :name "post/member" :value (:post/member post)})

                               (label {:for "post/body"} "body")
                               (input {:type "text" :name "post/body" :value (:post/body post)})

                               (label {:for "post/published-at"} "published-at")
                               (input {:type "text" :name "post/published-at" :value (:post/published-at post)})

                               (label {:for "post/slug"} "slug")
                               (input {:type "text" :name "post/slug" :value (:post/slug post)})

                               (label {:for "post/title"} "title")
                               (input {:type "text" :name "post/title" :value (:post/title post)})

                               (link-to (coast/url-for ::index) "Cancel")
                               (submit "Update post")))))

(defn change [request]
  (let [post       (coast/fetch :post (-> request :params :post-id))
        [_ errors] (-> (select-keys post [:post/id])
                       (merge (:params request))
                       (coast/validate [[:required [:post/id :post/member :post/body :post/published-at :post/slug :post/title]]])
                       (select-keys [:post/id :post/member :post/body :post/published-at :post/slug :post/title])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (edit (merge request errors)))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :post (-> request :params :post-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))
