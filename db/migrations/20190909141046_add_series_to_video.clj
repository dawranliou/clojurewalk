(ns migrations.20190909141046-add-series-to-video
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-reference :video :series))
