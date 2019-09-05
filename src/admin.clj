(ns admin
  (:require
   [buddy.hashers :as hashers]
   [coast]
   [components :refer [submit input link-to]]))

(defn sign-in
  [request]
  [:div.pa4.center.mw8
   [:h1 "Admin"]
   (when-let [error (:error/message request)]
     [:div error])
   (coast/form-for
     ::create
     (input {:type "email" :name "member/email"})
     (input {:type "password" :name "member/password"})
     (submit "Submit"))])

(defn create-session
  [request]
  (let [email           (get-in request [:params :member/email])
        member          (coast/find-by :member {:email email})
        [valid? errors] (-> (:params request)
                            (select-keys [:member/email :member/password])
                            (coast/validate [[:email [:member/email]]
                                             [:required [:member/email :member/password]]])
                            (get :member/password)
                            (hashers/check (:member/password member))
                            (coast/rescue))]
    (if (or (some? errors) (false? valid?))
      (sign-in (merge request {:error/message "Invalid email or password"}))
      (-> (coast/redirect-to ::dashboard)
          (assoc :session {:member/email email})))))

(defn delete-session
  [request]
  (-> (coast/redirect-to ::sign-in)
      (assoc :session nil)))

(defn dashboard
  [request]
  [:div.pa4.w-100.center.mw8
   [:h1 "Dashboard"]
   (link-to (coast/url-for :video/index) "Videos")
   (coast/form-for ::delete-session
                   (submit "Sign out"))])

(comment
  ;; sign up a admin user
  (-> #:member{:email    "admin@email.com"
               :password "password"}
      (update :member/password hashers/derive)
      (coast/insert)))
