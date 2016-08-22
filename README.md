# Processing Tweets with Kafka Streams in Scala 

The example application consists of two services written in Scala, an ingestion service ([code](https://github.com/jpzk/twitterstream/blob/master/twitterstream/src/main/scala/IngestApp.scala)) and an aggregation service ([code](https://github.com/jpzk/twitterstream/blob/master/twitterstream/src/main/scala/AggregationApp.scala)). The ingestion service subscribes to the [Twitter Streaming API](https://dev.twitter.com/streaming/overview) and receives fresh tweets filtered by a list of terms. Any raw tweet is sent to the Kafka topic 'tweets' in JSON. The aggregation service retrieves raw tweets, parses tweets, and aggregates word counts in tumbling time windows, see the code [here](https://github.com/jpzk/twitterstream/blob/master/twitterstream/src/main/scala/AggregationApp.scala). Kafka Streams uses an embedded [RocksDB](http://rocksdb.org/) for maintaining a local state. Any change to the aggregate will be propagated to the topic 'aggregate'.

Both services share the same [SBT](http://www.scala-sbt.org/index.html) project, and will be located in the same fat jar including all dependencies. Which allows us to easily share code in this small example project. Both applications access the [application.conf](https://github.com/jpzk/twitterstream/blob/master/application.conf.template) in runtime via the Settings object, see [code](https://github.com/jpzk/twitterstream/blob/master/twitterstream/src/main/scala/Settings.scala). I wrote a small [build script](https://github.com/jpzk/twitterstream/blob/master/build-run-containers.sh) to compile the services, building the Docker images and running the containers.

Read the [full article](https://www.madewithtea.com/processing-tweets-with-kafka-streams.html)

## Twitter Hosebird Client: References

* https://dev.twitter.com/streaming/overview
* https://github.com/twitter/hbc

## Kafka Streams: References
 
### Official Documentation

* http://www.confluent.io/blog/introducing-kafka-streams-stream-processing-made-simple
* https://kafka.apache.org/documentation.html
* http://docs.confluent.io/3.0.0/streams/javadocs/index.html
* http://docs.confluent.io/3.0.0/streams/developer-guide.html#kafka-streams-dsl

### Other Code Examples

* https://github.com/bbejeck/kafka-streams
* https://github.com/confluentinc/examples/blob/kafka-0.10.0.0-cp-3.0.0/kafka-streams/src/main/scala/io/confluent/examples/streams/MapFunctionScalaExample.scala

### Articles

* http://codingjunkie.net/kafka-processor-part1/
* http://codingjunkie.net/kafka-streams-part2/
* http://codingjunkie.net/kafka-streams-machine-learning/
* https://dzone.com/articles/machine-learning-with-kafka-streams
