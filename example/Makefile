IMAGE_CORD_PART:=fierycod/graalvm-native-image
USE_EE:=false
PWD=$$(pwd)
USER_GID=$$(id -u):$$(id -g)

ifeq ($(USE_EE), false)
	IMAGE_CORD=$(IMAGE_CORD_PART):ce
else
	IMAGE_CORD=$(IMAGE_CORD_PART):ee
endif

compile-agent:
	USE_AGENT_CONTEXT=true clojure -X:uberjar :aot true :jvm-opts '["-Dclojure.compiler.direct-linking=true", "-Dclojure.spec.skip-macros=true"]' :jar agent-output.jar :main-class example.core

native-gen-conf: compile-agent
	@docker run --user $(USER_GID) \
		-v $(PWD):/project \
		-it $(IMAGE_CORD) \
		bash -c "java -agentlib:native-image-agent=config-output-dir=resources/native-configuration \
			          -Dexecutor=native-agent \
			          -jar agent-output.jar"
compile:
	clojure -X:uberjar :aot true :jvm-opts '["-Dclojure.compiler.direct-linking=true", "-Dclojure.spec.skip-macros=true"]' :jar output.jar :main-class example.core

native-image: native-gen-conf compile
	@docker run --user $(USER_GID) \
		-v $(PWD):/project \
		-it $(IMAGE_CORD) \
		bash -c "native-image -jar output.jar \
		  -H:ConfigurationFileDirectories=resources/native-configuration \
		  -H:+AllowIncompleteClasspath \
		  --report-unsupported-elements-at-runtime \
          --no-fallback \
          --verbose \
          --enable-url-protocols=http,https \
          --no-server \
          --initialize-at-build-time"
