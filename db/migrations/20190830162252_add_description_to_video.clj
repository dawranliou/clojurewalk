(ns migrations.20190830162252-add-description-to-video
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :video :description :text))
