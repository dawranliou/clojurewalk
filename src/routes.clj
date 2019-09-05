(ns routes
  (:require [coast]
            [layouts]
            [middleware]
            [admin]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout layouts/layout
        [:get "/" :site.home/index]
        [:get "/vids" :site.video/index]
        [:get "/vids/:youtubeid" :site.video/player]

        [:get "/admin/sign-in" :admin/sign-in]
        [:post "/admin/sign-in" :admin/create-session]

        (coast/with
          middleware/auth
          [:resource :video]
          [:get "/admin" :admin/dashboard]
          [:post "/admin/sign-out" :admin/delete-session])))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
