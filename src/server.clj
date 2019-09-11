(ns server
  (:gen-class)
  (:require coast
            [coast.env :as env]
            [nrepl.server :as nrepl]
            routes))

(def app (coast/app {:routes routes/routes}))

(defn -main [& [port]]
  (when (= "prod"
           (env/env :coast-env))
    (println "nrepl starting on port 7888")
    (defonce server (nrepl/start-server :port 7888)))
  (coast/server app {:port port}))

(comment
  (-main))
