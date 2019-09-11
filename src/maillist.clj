(ns maillist
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))

(defn view [request]
  (let [id       (-> request :params :maillist-id)
        maillist (coast/fetch :maillist id)]
    (container {:mw 8}
               (dl
                (dt "display-name")
                (dd (:maillist/display-name maillist))

                (dt "email")
                (dd (:maillist/email maillist)))
               (mr2
                (link-to (coast/url-for :admin/dashboard) "List"))
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
                             (label {:for "maillist/display-name"} "display-name")
                             (input {:type "text" :name "maillist/display-name" :value (-> request :params :maillist/display-name)})

                             (label {:for "maillist/email"} "email")
                             (input {:type "text" :name "maillist/email" :value (-> request :params :maillist/email)})

                             (link-to (coast/url-for :admin/dashboard) "Cancel")
                             (submit "New maillist"))))

(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:maillist/display-name :maillist/email]]])
                       (select-keys [:maillist/display-name :maillist/email])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (build (merge request errors)))))

(defn edit [request]
  (let [maillist (coast/fetch :maillist (-> request :params :maillist-id))]
    (container {:mw 6}
               (when (some? (:errors request))
                 (errors (:errors request)))

               (coast/form-for ::change maillist
                               (label {:for "maillist/display-name"} "display-name")
                               (input {:type "text" :name "maillist/display-name" :value (:maillist/display-name maillist)})

                               (label {:for "maillist/email"} "email")
                               (input {:type "text" :name "maillist/email" :value (:maillist/email maillist)})

                               (link-to (coast/url-for :admin/dashboard) "Cancel")
                               (submit "Update maillist")))))

(defn change [request]
  (let [maillist   (coast/fetch :maillist (-> request :params :maillist-id))
        [_ errors] (-> (select-keys maillist [:maillist/id])
                       (merge (:params request))
                       (coast/validate [[:required [:maillist/id :maillist/display-name :maillist/email]]])
                       (select-keys [:maillist/id :maillist/display-name :maillist/email])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (edit (merge request errors)))))

(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :maillist (-> request :params :maillist-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to :admin/dashboard)
      (-> (coast/redirect-to :admin/dashboard)
          (coast/flash "Something went wrong!")))))
