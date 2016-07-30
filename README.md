# Twitter Streaming API Example with Kafka Streams in Scala (wip)

This worker app reads from Twitter's Streaming API and pushes the raw JSON status messages filtered by filter terms to a specified Kafka topic. The aggregation apps read from the Kafka topic, and does aggregation with Kafka Streams on the data.

## Twitter Hosebird Client: References

* https://dev.twitter.com/streaming/overview
* 

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
