package mwt.twitterstream

import com.twitter.conversions.time._
import org.apache.kafka.streams.{KafkaStreams, KeyValue}
import org.apache.kafka.streams.kstream._

case class WindowedWordHistogram(start: Long, end: Long, histogram: Map[String, Int])

object AggregationApp extends App {

  class WordHistogramInitializer extends Initializer[Map[String, Int]] {
    override def apply(): Map[String, Int] = Map()
  }

  class WordHistogramAggregator extends Aggregator[TweetKey, Array[String], Map[String, Int]] {
    override def apply(aggKey: TweetKey, value: Array[String], aggregate: Map[String, Int]) = {
      // within-list frequencies
      val frequencies = value
        .groupBy[String] { word => word }
        .map { case (word, instances) => (word -> instances.length) }

      // add frequencies to global word frequencies
      val updates = frequencies.keys.map { case key =>
        if (aggregate.contains(key)) (key, aggregate(key) + frequencies(key)) else (key, 1)
      }

      aggregate ++ updates
    }
  }

  val (builder, properties) = Settings.kafkaStreamSource

  val out = builder.stream(new JSONSerde[TweetKey], new JSONSerde[Tweet], Settings.rawTopic)
    .mapValues(new ValueMapper[Tweet, Array[String]] {
      override def apply(value: Tweet): Array[String] = 
        value.text
          .toLowerCase
          .split(" ")
          .map { word => word.trim }
    })
    .aggregateByKey(
      new WordHistogramInitializer(),
      new WordHistogramAggregator(),
      TimeWindows.of("WORD_HISTOGRAM", (10.minutes).inMillis),
      new JSONSerde[TweetKey],
      new JSONSerde[Map[String, Int]])
    .toStream
    .map {
      new KeyValueMapper[Windowed[TweetKey], Map[String, Int], KeyValue[TweetKey, WindowedWordHistogram]] {
        override def apply(key: Windowed[TweetKey], value: Map[String, Int]) = {
          new KeyValue(key.key(), WindowedWordHistogram(key.window.start(), key.window.end(), value))
        }
      }
    }

  out.print()
  out.to(new JSONSerde[TweetKey], new JSONSerde[WindowedWordHistogram], Settings.aggregationTopic)

  val stream: KafkaStreams = new KafkaStreams(builder, properties)
  stream.start()
}
