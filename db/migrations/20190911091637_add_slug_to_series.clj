(ns migrations.20190911091637-add-slug-to-series
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :series :slug :text))
