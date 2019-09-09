(ns migrations.20190909102754-create-table-maillist
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :maillist
                (text :email)
                (text :display-name)
                (timestamps)))
