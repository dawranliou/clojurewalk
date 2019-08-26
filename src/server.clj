(ns server
  (:require [coast]
            [coast.env :as env]
            [routes]
            [nrepl.server :as nrepl])
  (:gen-class))

(def app (coast/app {:routes routes/routes}))

(defn -main [& [port]]
  (when (= "prod"
           (env/env :coast-env))
    (println "nrepl starting on port 7888")
    (defonce server (nrepl/start-server :port 7888)))
  (coast/server app {:port port}))

(comment
  (-main))
