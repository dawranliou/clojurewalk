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
