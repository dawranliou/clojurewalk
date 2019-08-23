(ns package
  (:require [badigeon.bundle :refer [bundle make-out-path]]
            [badigeon.compile :as c]))

(defn -main []
  (bundle (make-out-path 'app nil) {:libs-path "jars"})
  (c/compile '[server] {:compile-path     "target/classes"
                        :compiler-options {:disable-locals-clearing false
                                           :elide-meta              [:doc :file :line :added]
                                           :direct-linking          true}}))
