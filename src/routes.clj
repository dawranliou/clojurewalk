(ns routes
  (:require [coast]
            [components]
            [middleware]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout components/layout
        [:get "/" :site.home/index]

        [:get "/admin/sign-in" :admin/build]
        [:post "/admin/sign-in" :admin/create]

        (coast/with
          middleware/auth
          [:resource :video]
          [:get "/admin" :admin/dashboard]
          [:post "/admin/sign-out" :admin/delete])))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
