package mwt.twitterstream

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams

object AggregationApp extends App {

  val (builder, properties) = Settings.kafkaStreamSource

  val tweets = builder.stream(Serdes.ByteArray(), new JSONSerde[Tweet], Settings.rawTopic)
  tweets.print()

  val stream: KafkaStreams = new KafkaStreams(builder, properties)

  stream.start()
}
