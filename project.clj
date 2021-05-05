(defproject io.github.FieryCod/clojure-graalvm-agent-helper   "0.0.1"
  :description "Hacky solution to generate configurations for GraalVM native-image"

  :url "https://github.com/FieryCod/clojure-graalvm-agent-helper"

  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :source-paths ["src"]

  :global-vars {*warn-on-reflection* true}

  :dependencies [[org.clojure/clojure "1.10.3" :scope "provided"]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :creds :gpg}
                         "snapshots" {:url "https://clojars.org/repo"
                                      :creds :gpg}]]
  :scm {:name "git"
        :url "https://github.com/FieryCod/clojure-graalvm-agent-helper"})
