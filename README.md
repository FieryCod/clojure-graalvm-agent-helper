# clojure-graalvm-agent-helper
Ease native configuration generation for GraalVM NativeImage. You don't have to write configurations for native image by hand, since there is [Agent](https://medium.com/graalvm/introducing-the-tracing-agent-simplifying-graalvm-native-image-configuration-c3b56c486271) which might trace all the calls you make. 

This tool helps you automate the process. You can leave in-context calls and production version of the jar will not contain any code which in-context wraps.

Extracted from `holy-lambda` micro-framework.

## How to use it?
1. Add following dependency:

   ``` clojure
    io.github.FieryCod/clojure-graalvm-agent-helper {:mvn/version "0.0.1"}
   ```
   
   2. Use `agent/in-context` in your code:
   
   ```clojure
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
   ```
   
   3. Compile with `USE_AGENT_CONTEXT` environment set to `true`, so that `in-context` will not be removed
   
   ```
   USE_AGENT_CONTEXT=true clojure -X:uberjar :aot true :jvm-opts '["-Dclojure.compiler.direct-linking=true", "-Dclojure.spec.skip-macros=true"]' :jar agent-output.jar :main-class example.core
   ```
   
   4. Run in native agent context
   ```
   java -agentlib:native-image-agent=config-output-dir=resources/native-configuration \
        -Dexecutor=native-agent \
        -jar agent-output.jar
   ```
   
   5. Compile a project without `USE_IN_AGENT_CONTEXT`
   ```
   clojure -X:uberjar :aot true :jvm-opts '["-Dclojure.compiler.direct-linking=true", "-Dclojure.spec.skip-macros=true"]' :jar output.jar :main-class example.core
   ```
   
   5. Feed `native-image` with configuration and compile
   ```
   native-image -jar output.jar \
     -H:ConfigurationFileDirectories=resources/native-configuration \
     -H:+AllowIncompleteClasspath \
     --report-unsupported-elements-at-runtime \
     --no-fallback \
     --verbose \
     --enable-url-protocols=http,https \
     --no-server \
     --initialize-at-build-time 
   ```
   
   Check example folder if you still have trouble setting it up.
