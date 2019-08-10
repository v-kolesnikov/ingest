(ns ingest.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [com.tectonica.jonix Jonix]
           [com.tectonica.jonix.json JonixJson]))

(defn file->records [file] (. Jonix source file "*.xml" true))

(comment
  (let [records (file->records (io/file "resources/22"))]
    (let [json (->> records
                    (map #(.product %))
                    (map #(. JonixJson productToJson %))
                    (str/join))]
      (spit "products.json" json))))
