(ns associations
  (:require [coast.db.associations :refer [table belongs-to has-many tables]]))

(defn associations []
  (tables
   (table :video
          (belongs-to :series))
   (table :series
          (has-many :videos))))
