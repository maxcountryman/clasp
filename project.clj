(defproject clasp "0.0.1"
  :description "A dead simple routing DSL for Clojure's ring."
  :url "https://github.com/maxcountryman/clasp"
  :license {:name "BSD"
            :url "http://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[clout "1.1.0"]
                 [org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.6"]
                 [ring/ring-jetty-adapter "1.1.6"]]
  :plugins [[lein-marginalia "0.7.1"]])
