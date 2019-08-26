(ns middleware
  (:require
   [coast]))

(defn auth
  [handler]
  (fn [request]
    (if (get-in request [:session :admin])
      (handler request)
      (coast/redirect-to :admin/sign-in))))
