(ns site.article
  (:require coast
            [components :refer [container]]
            helpers
            [markdown.core :as markdown]))

(defn index
  [request]
  (let [posts (coast/q '[:select *
                         :from post
                         :where ["published_at is not null"]
                         :order published_at desc])]
    (container
     {:mw 7}
     (for [{:post/keys [title body published-at] :as post} posts]
       [:article.mb5
        [:time.f6.gray.mb1.dib
         {:data-seconds published-at
          :data-date    true}
         (coast/strftime
          (coast/datetime published-at "US/Mountain")
          "MMMM dd, YYYY")]
        [:h1.ma0 title]
        [:p.pb0.mb1 (helpers/ellipsis body 200)]
        [:a.underline.blud
         {:href (coast/url-for ::reader post)}
         "Read More"]]))))

(defn reader
  [request]
  (let [slug                                   (-> request :params :post-slug)
        post                                   (coast/pluck '[:select *
                                                              :from post
                                                              :where [slug ?slug] ["published_at is not null"]]
                                                            {:slug slug})
        {:post/keys [published-at title body]} post]
    (if (nil? post)
      (coast/raise {:not-found true})
      (container
       {:mw 7}
       [:article
        [:time.f6.gray.mb1.dib
         {:data-seconds published-at
          :data-date    true}
         (coast/strftime
          (coast/datetime published-at "US/Mountain")
          "MMMM dd, YYYY")]
        [:h1.ma0 title]
        (coast/raw
         (markdown/md-to-html-string body))]))))
