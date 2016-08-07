package mwt.twitterstream

import sys.addShutdownHook
import org.apache.kafka.clients.producer.ProducerRecord

object IngestApp extends App with Logging {

  var closing = false;

  // close gracefully
  addShutdownHook {
    closing = true
    producer.close
    source.hosebirdClient.stop
  }

  log.info(Settings.config.toString)

  val source = Settings.tweetSource
  val producer = Settings.kafkaProducer
  val topic = Settings.rawTopic
  val partition = Settings.partition

  while (!(source.hosebirdClient.isDone) & !(closing)) {
    source.take() match {
      case Some(json) => 
        send(json)
      case None =>
    }
  }

  def send(msg: String): Unit = {
    val ts = System.currentTimeMillis()
    val key = TweetKey(Settings.filterTerms)
    val keyPayload = Json.String.encode(key).map(_.toByte).toArray
    val payload = msg.map(_.toByte).toArray
    val record = new ProducerRecord[Array[Byte], Array[Byte]](topic, partition, ts, keyPayload, payload)
    log.info(s"Sending to Kafka ${record}")
    producer.send(record)
  }
}
