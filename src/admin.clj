(ns admin
  (:require
   [buddy.hashers :as hashers]
   [coast]
   [components :refer [container submit input link-to table thead tbody th tr td]]))

(defn sign-in
  [request]
  (container
    {:mw 6}
    [:h1 "Admin"]
    (when-let [error (:error/message request)]
      [:div error])
    (coast/form-for
      ::create
      (input {:type "email" :name "member/email"})
      (input {:type "password" :name "member/password"})
      (submit "Submit"))))

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
  (container
    {:mw 6}
    [:h1.dib "Dashboard"]
    (table
      (tbody (tr (td (link-to (coast/url-for :video/index) "Videos")))
             (tr (td "Articles"))
             (tr (td "About"))))

    [:div.mv3
     (coast/form-for ::delete-session
                     (submit "Sign out"))]))

(comment
  ;; sign up a admin user
  (-> #:member{:email    "admin@email.com"
               :password "password"}
      (update :member/password hashers/derive)
      (coast/insert)))
