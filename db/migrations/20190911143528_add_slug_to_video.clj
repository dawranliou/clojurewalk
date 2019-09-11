(ns migrations.20190911143528-add-slug-to-video
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :video :slug :text))
