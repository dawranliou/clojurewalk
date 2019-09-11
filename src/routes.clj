(ns routes
  (:require [coast]
            [layouts]
            [middleware]
            [admin]))

(def routes
  (coast/routes

   (coast/site
    (coast/with-layout layouts/doc-layout
      [:get "/read/:post-slug" :site.article/reader])

    (coast/with-layout layouts/layout
      ;; home
      [:get "/" :site.home/index]

      ;; videos
      [:get "/watch" :site.video/index]
      [:get "/watch/:youtubeid" :site.video/player]

      ;; articles
      [:get "/read" :site.article/index]

      ;; about
      [:get "/about" :site.about/index]

      ;; subscribe-to-newsletter
      [:post "/subscribe-to-newsletter" :site.about/subscribe]

      ;; admin
      [:get "/admin/sign-in" :admin/sign-in]
      [:post "/admin/sign-in" :admin/create-session]

      (coast/with
       middleware/auth middleware/current-member

       [:get "/admin" :admin/dashboard]
       [:post "/admin/sign-out" :admin/delete-session]

       [:get    "/videos"          :video/index]
       [:get    "/videos/:id"      :video/view]
       [:get    "/videos/build"    :video/build]
       [:get    "/videos/:id/edit" :video/edit]
       [:post   "/videos"          :video/create]
       [:put    "/videos/:id"      :video/change]
       [:delete "/videos/:id"      :video/delete]

       [:get    "/maillists"          :maillist/index]
       [:get    "/maillists/:id"      :maillist/view]
       [:get    "/maillists/build"    :maillist/build]
       [:get    "/maillists/:id/edit" :maillist/edit]
       [:post   "/maillists"          :maillist/create]
       [:put    "/maillists/:id"      :maillist/change]
       [:delete "/maillists/:id"      :maillist/delete]

       [:get    "/seriess"          :series/index]
       [:get    "/seriess/:id"      :series/view]
       [:get    "/seriess/build"    :series/build]
       [:get    "/seriess/:id/edit" :series/edit]
       [:post   "/seriess"          :series/create]
       [:put    "/seriess/:id"      :series/change]
       [:delete "/seriess/:id"      :series/delete]

       [:get    "/posts"          :post/index]
       [:get    "/posts/:id"      :post/view]
       [:get    "/posts/build"    :post/build]
       [:get    "/posts/:id/edit" :post/edit]
       [:post   "/posts"          :post/create]
       [:put    "/posts/:id"      :post/change]
       [:delete "/posts/:id"      :post/delete]

       [:post "/posts/preview" :post/preview])))

   (coast/api
    (coast/with-prefix "/api"
      [:get "/" :api.home/index]))))
