(ns migrations.20190823115816-create-table-video
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :video
                (text :title)
                (text :youtubeid)
                (timestamps)))
