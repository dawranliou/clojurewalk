(ns routes
  (:require [coast]
            [components]
            [middleware]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout components/layout
        [:get "/" :site.home/index]

        (coast/with-prefix "/admin"
          [:get "/sign-in" :admin/build]
          [:post "/sign-in" :admin/create]
          (coast/with middleware/auth
                      [:get "/dashboard" :admin/dashboard]
                      [:resource :video]
                      [:post "/sign-out" :admin/delete]))))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
