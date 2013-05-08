(defproject fsm "0.1.0"
  :description "A state machine library inspired by waltz"
  :plugins [[lein-cljsbuild "0.3.0"]]
  :cljsbuild {:builds {:dev {:source-paths ["src"]
                             :compiler {:output-to "resources/public/js/core.js"
                                        :output-dir ".clojurescript-output"}}
                       :test {:source-paths ["test"]
                              :compiler {:pretty-print true
                                         :output-to "resources/private/js/unit-test.js"
                                         :optimizations :whitespace}}}
              :test-commands {"unit" ["phantomjs"
                                      "phantom/unit-test.js"
                                      "resources/private/html/unit-test.html"]}})
