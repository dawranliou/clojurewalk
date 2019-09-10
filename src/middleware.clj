(ns middleware
  (:require
   [coast]
   [coast.responses]))

(defn auth
  [handler]
  (fn [request]
    (if (get-in request [:session :member/email])
      (handler request)
      (coast.responses/forbidden "Sorry pal, you are forbidden here."))))

(defn current-member
  [handler]
  (fn [request]
    (let [email  (get-in request [:session :member/email])
          member (coast/find-by :member {:member/email email})]
      (handler (assoc request :member member)))))
