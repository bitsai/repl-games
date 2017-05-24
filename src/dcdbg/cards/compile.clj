(ns dcdbg.cards.compile
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [clojure.string :as str]))

(def config
  {:name {:index 0
          :parse-fn #(str/replace % "Impossible Mode" "IM")}
   :type {:index 1
          :parse-fn #(-> % (str/replace " " "-") str/lower-case keyword)}
   :cost {:index 2}
   :victory {:index 3}
   :text {:index 9}
   :copies {:index 10
            :parse-fn #(Long. %)}})

(defn- get-card-files []
  (-> "resources/dcdbg"
      (io/file)
      (file-seq)
      (rest)))

(defn- parse-field [[k {:keys [index parse-fn]}] row]
  (let [parse-fn (or parse-fn identity)
        v (get row index)]
    (when (seq v)
      [k (parse-fn v)])))

(defn- parse-csv-row [row set]
  (when (-> row first seq)
    (->> config
         (keep #(parse-field % row))
         (into {:set set}))))

(defn- parse-csv-file [file]
  (let [set (-> file (.getName) (str/split #"\.") first keyword)]
    (->> file slurp csv/read-csv rest (keep #(parse-csv-row % set)))))

(defn compile-cards! []
  (let [tmpl "(ns dcdbg.cards.compiled)\n\n(def cards\n%s)\n"]
    (->> (get-card-files)
         (mapcat parse-csv-file)
         (vec)
         (#(with-out-str (pprint/pprint %)))
         (format tmpl)
         (spit "src/dcdbg/cards/compiled.clj"))))
