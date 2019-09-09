(ns migrations.20190909135558-create-table-series
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :series
    (text :name)
    (timestamps)))