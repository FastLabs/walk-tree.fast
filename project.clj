(defproject walk-tree "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"]
                 [re-frame "0.10.7"]
                 [reagent "0.8.1"]
                 [martian-re-frame "0.1.11"]]


  :resource-paths ["resources"]

  :aliases {"fig"       ["run" "-m" "figwheel.main"]
            "fig:build" ["run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "walk-tree.test-runner"]}

  :profiles {:dev {:source-paths ["dev"]
                   :clean-targets ^{:protect false} ["resources/public/cljs-out" "target"]
                   :dependencies [[compojure "1.6.1"]
                                  [ring-middleware-format "0.7.4"]
                                  [devcards "0.2.6"]
                                  [com.bhauman/figwheel-main "0.2.3"]]}})


