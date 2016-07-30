package mwt.twitterstream

import java.io.File
import java.util.{Properties, UUID}

import com.twitter.hbc.httpclient.auth.OAuth1
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStreamBuilder

object Settings {

  val config = ConfigFactory.parseFile(new File("application.conf"))
  val kConfig = config.getConfig("kafka")
  val tConfig = config.getConfig("twitter")

  def filterTerms = {
    val terms = tConfig.getStringList("terms")
    Range(0, terms.size()).map { i => terms.get(i) }
  }

  def zookeepers = kConfig.getString("zookeepers")

  def brokers = kConfig.getString("brokers")

  def rawTopic = kConfig.getString("raw_topic")

  def aggregationTopic = kConfig.getString("aggregation_topic")

  def partition = kConfig.getInt("partition")

  def stateDir = kConfig.getString("state_dir")

  def kafkaProducer = {
    val props = new Properties()
    val serde = "org.apache.kafka.common.serialization.ByteArraySerializer"
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serde)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serde)
    new KafkaProducer[Array[Byte], Array[Byte]](props)
  }

  def kafkaStreamSource = {
    val builder: KStreamBuilder = new KStreamBuilder

    val streamingConfig = {
      val settings = new Properties
      settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      settings.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString)
      settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
      settings.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zookeepers)
      settings.put(StreamsConfig.STATE_DIR_CONFIG, stateDir)
      settings
    }

    (builder, streamingConfig)
  }

  def tweetSource = {
    val oAuth1 = new OAuth1(
      tConfig.getString("consumer_key"),
      tConfig.getString("consumer_secret"),
      tConfig.getString("token"),
      tConfig.getString("token_secret"))

    new TweetSource(oAuth1, filterTerms)
  }
}
