(ns dcdbg.cards.compile
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [clojure.string :as str]))

(def csv-fields
  {:name {:index 0
          :parse-fn #(str/replace % "Impossible Mode" "IM")}
   :type {:index 1
          :parse-fn #(-> % (str/replace " " "-") str/lower-case keyword)}
   :cost {:index 2
          :parse-fn #(try
                       (Long. %)
                       (catch Throwable t
                         %))}
   :victory {:index 3
             :parse-fn #(try
                          (Long. %)
                          (catch Throwable t
                            %))}
   :copies {:index 10
            :parse-fn #(Long. %)}})

(def card-text-index 9)

(def card-text-sections
  [{:re #"\s+TO BEAT:\s+" :k :to-beat}
   {:re #"\s+FIRST APPEARANCE -- ATTACK:\s+" :k :attack}
   {:re #"\s*Attack:\s+" :k :attack}
   {:re #"\s*Stack Ongoing:\s+" :k :ongoing}
   {:re #"\s*ONGOING:\s+" :k :ongoing}
   {:re #"\s*Ongoing:\s+" :k :ongoing}
   {:re #"\s*Defense:\s+" :k :defense}])

(def card-power-res
  [#"^\+(\d) Power$"
   #"^\+(\d) Power\.\s*"
   #"^\+(\d) Power,?\s+and\s+"])

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

(defn- parse-card-power [{:keys [text] :as parsed}]
  (if-let [re (first (filter #(re-find % text) card-power-res))]
    (let [[_ y] (str/split text re)
          [_ power] (re-find re text)]
      (assoc parsed :text (or y "") :power (Long. power)))
    parsed))

(defn- parse-variable-power [{:keys [text power] :as parsed}]
  (cond-> parsed
    (and (re-find #"\+\d Power" text) (not power))
    (assoc :power "*")))

(defn- remove-empty-text [{:keys [text] :as parsed}]
  (cond-> parsed
    (empty? text)
    (dissoc :text)))

(defn- parse-card-text [row]
  (-> (reduce (fn [{:keys [text] :as acc} {:keys [re k]}]
                (if-not (re-find re text)
                  acc
                  (let [[x y] (str/split text re)]
                    (assoc acc :text x k y))))
              {:text (get row card-text-index)}
              card-text-sections)
      (parse-card-power)
      (parse-variable-power)
      (remove-empty-text)))

(defn- parse-csv-row [row set]
  (when (-> row first seq)
    (merge (->> csv-fields
             (keep #(parse-field % row))
             (into {:set set}))
           (parse-card-text row))))

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
