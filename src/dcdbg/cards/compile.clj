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

(defn- parse-card-text [row]
  (let [text (get row card-text-index)]
    (when (seq text)
      (reduce (fn [{:keys [text] :as acc} {:keys [re k]}]
                (if-not (and text (re-find re text))
                  acc
                  (let [[x y] (str/split text re)
                        acc (assoc acc k y)]
                    (if (seq x)
                      (assoc acc :text x)
                      (dissoc acc :text)))))
              {:text text}
              card-text-sections))))

(defn- parse-card-power [parsed]
  (if-let [text (:text parsed)]
    (if-let [re (first (filter #(re-find % text) card-power-res))]
      (let [[_ y] (str/split text re)
            [_ power] (re-find re text)
            parsed (assoc parsed :power (Long. power))]
        (if (seq y)
          (assoc parsed :text y)
          (dissoc parsed :text)))
      parsed)
    parsed))

(defn- parse-csv-row [row set]
  (when (-> row first seq)
    (-> (->> csv-fields
             (keep #(parse-field % row))
             (into {:set set}))
        (merge (parse-card-text row))
        (parse-card-power))))

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
