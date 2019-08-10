(ns ingest.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [com.tectonica.jonix Jonix]
           [com.tectonica.jonix.json JonixJson]))

(defn file->records [file] (. Jonix source file "*.xml" true))

(comment
  ; Streaming records
  ; https://github.com/zach-m/jonix#from-iteration-to-streaming
  ; https://stackoverflow.com/questions/35574155/consuming-java-streams-in-clojure
  (let [records (file->records (io/file "resources/onix"))
        stream (-> records .stream .iterator iterator-seq)]
    (last stream))

  ; Map to JSON's
  (let [records (file->records (io/file "resources/onix"))
        stream (-> records .stream .iterator iterator-seq)
        json (->> stream
                  (map #(.product %))
                  (map #(. JonixJson productToJson %)))]
    (doseq [product json]
      (println product)))

  ; Write record to products.json
  (let [records (file->records (io/file "resources/onix"))
        json (->> records
                  (map #(.product %))
                  (map #(. JonixJson productToJson %))
                  (str/join))]
    (spit "products" json)))
