(ns migrations.20190910081615-create-table-post
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :post
                (references :member)
                (text :title)
                (text :body)
                (text :slug)
                (integer :published-at)
                (timestamps)))
