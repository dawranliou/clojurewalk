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
      [:get "/watch/series/:series-slug" :site.video/series]

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

       [:get    "/videos"           :video/index]
       [:get    "/videos/:video-id" :video/view]
       [:get    "/videos/build"    :video/build]
       [:get    "/videos/:video-id/edit" :video/edit]
       [:post   "/videos"          :video/create]
       [:put    "/videos/:video-id" :video/change]
       [:delete "/videos/:video-id" :video/delete]

       [:get    "/maillists"          :maillist/index]
       [:get    "/maillists/:maillist-id"      :maillist/view]
       [:get    "/maillists/build"    :maillist/build]
       [:get    "/maillists/:maillist-id/edit" :maillist/edit]
       [:post   "/maillists"          :maillist/create]
       [:put    "/maillists/:maillist-id"      :maillist/change]
       [:delete "/maillists/:maillist-id"      :maillist/delete]

       [:get    "/series"          :series/index]
       [:get    "/series/:series-id"      :series/view]
       [:get    "/series/build"    :series/build]
       [:get    "/series/:series-id/edit" :series/edit]
       [:post   "/series"          :series/create]
       [:put    "/series/:series-id"      :series/change]
       [:delete "/series/:series-id"      :series/delete]

       [:get    "/posts"          :post/index]
       [:get    "/posts/:post-id"      :post/view]
       [:get    "/posts/build"    :post/build]
       [:get    "/posts/:post-id/edit" :post/edit]
       [:post   "/posts"          :post/create]
       [:put    "/posts/:post-id"      :post/change]
       [:delete "/posts/:post-id"      :post/delete]

       [:post "/posts/preview" :post/preview])))

   (coast/api
    (coast/with-prefix "/api"
      [:get "/" :api.home/index]))))
