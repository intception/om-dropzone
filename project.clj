(defproject org.clojars.intception/om-dropzone "0.1.1"
  :description "An Om (ClojureScript) dropzone component"
  :url "http://github.com/intception/om-dropzone"
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :jvm-opts ^:replace ["-Xms512m" "-Xmx512m" "-server"]

  :source-paths  ["src"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.319.0-6b1aca-alpha"]
                 [org.clojure/clojurescript "0.0-2356" :scope "provided"]
                 [om "0.7.1"]
                 [prismatic/schema "0.2.6"]
                 [net.unit8/tower-cljs "0.1.0"]]

  :min-lein-version "2.0.0"

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:preamble ["react/react.min.js"]
                                   :output-to "script/tests.simple.js"
                                   :output-dir "script/out"
                                   :source-map "script/tests.simple.js.map"
                                   :output-wrapper false
                                   :optimizations :simple}}
                       ;; examples
                       {:id "basic"
                        :source-paths ["src" "examples/basic/src"]
                        :compiler {:output-to "examples/basic/main.js"
                                   :output-dir "examples/basic/out"
                                   :source-map true
                                   :optimizations :none}}]})