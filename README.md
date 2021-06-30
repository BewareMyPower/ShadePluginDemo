# ShadePluginDemo

An example to use [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/).

## Get started

Make sure you have already run a local [Kafka]() broker that listens at localhost:9092.

First build the project:

```bash
mvn clean install
```

Then run the demo:

```bash
mvn exec:java -Dexec.mainClass=Demo -pl demo
```

## What's this?

It's an example that makes a class use variant versions of Kafka client. Usually we can't import the same dependency with multiple versions. However, with the help of maven-shade-plugin, we can archive the similar result.

For example, this project has multiple modules:
- kafka-1-0-0
- kafka-2-0-0

Each module wraps a different version of Kafka client. Without the maven-shade-plugin, the `demo` module, which imports these modules as the dependencies, will only choose **one** version of `org.apache.kafka:kafka-clients`.

## Wrong version and commit id

You may see followed logs.

```
2021-07-01 00:51:37:581 [Demo.main()] INFO org.apache.kafka-1-0-0.common.utils.AppInfoParser - Kafka version : 1.0.0
2021-07-01 00:51:37:581 [Demo.main()] INFO org.apache.kafka-1-0-0.common.utils.AppInfoParser - Kafka commitId : aaa7af6d4a11b29d
```

It's okay because `AppInfoParser` reads the version and commit id from the same `/kafka/kafka-version.properties`.
