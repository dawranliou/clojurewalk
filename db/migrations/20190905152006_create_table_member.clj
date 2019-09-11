(ns migrations.20190905152006-create-table-member
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :member
                (text :email)
                (text :display-name)
                (text :password)
                (timestamps)))
