# Generative AI API

Generative AI API generates review tips and Question answers using OpenApi.


## Table of Contents
* [Running Locally](#RunningLocally)
* [APIs](#APIs)

## <a name="RunningLocally" />Running Locally

You can run this service locally with `IntelliJ / Commandline`.

### IntelliJ ###

Running with IntelliJ is fairly simple, as it will automatically configure the run configuration.

### Commandline ###

Build the project first with maven.

```mvn clean install```

Then run the jar file with java runtime.

```java -jar -Dspring.profiles.active=local target/generative-ai-api-0.0.1-SNAPSHOT.jar```


## <a name="APIs" />APIs

```/data/reviewtip```
