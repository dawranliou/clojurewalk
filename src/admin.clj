(ns admin
  (:require
   [buddy.hashers :as hashers]
   [coast]
   [coast.env :as env]))

(defn build
  [request]
  [:div
   (when-let [error (:error/message request)]
     [:div error])
   (coast/form-for ::create
                   [:input {:type "text" :name "username"}]
                   [:input {:type "password" :name "password"}]
                   [:input {:type "submit" :value "Submit"}])])

(defn create
  [request]
  (let [input  (:params request)
        uname  (env/env :admin-uname)
        pw     (env/env :admin-pw)
        valid? (and (= uname (:username input))
                    (hashers/check (:password input) pw))]
    (if valid?
      (-> (coast/redirect-to ::dashboard)
          (assoc :session {:admin true}))
      (build (merge request {:error/message "Invalid username or password"})))))

(defn delete
  [request]
  (-> (coast/redirect-to ::build)
      (dissoc :session)))

(defn dashboard
  [request]
  [:div
   [:h1 "Dashboard"]
   (coast/form-for ::delete
                   [:input {:type "submit" :value "Sign out"}])])
