(ns admin
  (:require
   [buddy.hashers :as hashers]
   [coast]
   [coast.env :as env]))

(defn input
  [{:keys [type name] :as m}]
  [:div.mv3
   [:label.db.fw6.lh-copy.f6 name]
   [:input.pa2.input-reset.ba.bg-transparent
    m]])

(defn sign-in
  [request]
  [:div.pa4.w-100.center.mw8
   [:h1 "Admin"]
   (when-let [error (:error/message request)]
     [:div error])
   (coast/form-for
     ::create
     (input {:type "text" :name "username"})
     (input {:type "password" :name "password"})
     (input {:type "submit" :value "Submit"}))])

(defn create-session
  [request]
  (let [input  (:params request)
        uname  (env/env :admin-uname)
        pw     (env/env :admin-pw)
        valid? (and (= uname (:username input))
                    (hashers/check (:password input) pw))]
    (if valid?
      (-> (coast/redirect-to ::dashboard)
          (assoc :session {:admin true}))
      (sign-in (merge request {:error/message "Invalid username or password"})))))

(defn delete-session
  [request]
  (-> (coast/redirect-to ::sign-in)
      (assoc :session nil)))

(defn dashboard
  [request]
  [:div.pa4.w-100.center.mw8
   [:h1 "Dashboard"]
   (coast/form-for ::delete-session
                   [:input {:type "submit" :value "Sign out"}])
   [:a.f6.link.underline.blue
    {:href (coast/url-for :video/index)}
    "Videos"]])
