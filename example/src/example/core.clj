(ns example.core
  (:gen-class)
  (:require
   [fierycod.graalvm-agent-helper.core :as agent]))

(agent/in-context
 (println "I can run arbitrary code in agent context. Agent will catch all the calls in context and generate native configuration out of it :)"))

(agent/in-context
 (println "In case of error agent will catch it :)")
 (throw (ex-info "Ups.." {})))

(defn -main
  []
  (agent/in-context (println "Since main is called I will print as well :)")))
