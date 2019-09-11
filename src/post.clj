(ns post
  (:require [clojure.string :as string]
            coast
            [components
             :refer
             [button-to
              container
              dd
              dl
              dt
              errors
              form
              link-to
              mr2
              markdown-editor
              table
              tbody
              tc
              td
              th
              thead
              tr]]
            [markdown.core :as markdown]))

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
                    (th "id")
                    (th "member")
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
                      (td (:post/id row))
                      (td (:post/member row))
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

(defn preview [request]
  (let [{:keys [title body]} (:params request)]
    (coast/ok
     [:article
      [:h1.ma0 title]
      (coast/raw (markdown/md-to-html-string body))]
     :html)))

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
    (form (coast/action-for ::create) request :post/title :post/body)]))

(defn slug [s]
  (str (-> (.toLowerCase s)
           (string/replace #"\s+" "-")
           (string/replace #"[^\w\-]+" "")
           (string/replace #"\-\-+" "-")
           (string/replace #"^-+" "")
           (string/replace #"-+$" ""))
       "-" (last (string/split (str (coast/uuid)) #"-"))))

(defn create [{:keys [member params] :as request}]
  (let [xhr?          (coast/xhr? request)
        params        (if xhr?
                        params
                        (assoc params :post/published-at (coast/now)))
        params        (assoc params :post/slug (slug (:post/title (:params request))))
        [post errors] (-> (merge params {:post/member (:member/id member)})
                          (coast/validate [[:required [:post/member]]])
                          (select-keys [:post/member :post/body :post/published-at :post/slug :post/title])
                          (coast/insert)
                          (coast/rescue))]
    (def xhr? xhr?)
    (if (nil? errors)
      (if xhr?
        (coast/ok {:form-params (coast/action-for ::change post)
                   :url         (coast/url-for ::edit post)}
                  :json)
        (coast/redirect-to ::index))
      (if xhr?
        (coast/server-error (form (coast/action-for ::create) (merge request errors) :post/title :post/body))
        (build (merge request errors))))))

(defn edit [request]
  (let [post (coast/fetch :post (-> request :params :post-id))]
    (container {:mw 7}
               (markdown-editor (coast/action-for ::change post) {:params post} :post/title :post/body))))

(defn change [{:keys [params] :as request}]
  (def params params)
  (let [post       (coast/fetch :post (:post-id params))
        post       (if (some? (:post/slug post))
                     post
                     (assoc post :post/slug (slug (:post/title params))))
        post       (case (:submit params)
                     "Publish"    (if (some? (:post/published-at post))
                                    post
                                    (assoc post :post/published-at (coast/now)))
                     "Un-publish" (assoc post :post/published-at nil)
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
        (coast/redirect-to ::index)
        (edit (merge request errors))))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :post (-> request :params :post-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))
