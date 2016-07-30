package mwt.twitterstream

import sys.addShutdownHook
import org.apache.kafka.clients.producer.ProducerRecord

object IngestApp extends App with Logging {

  log.info(Settings.config.toString)

  val source = Settings.tweetSource
  val producer = Settings.kafkaProducer
  val topic = Settings.rawTopic
  val partition = Settings.partition

  addShutdownHook {
    producer.close
  }

  while (!source.hosebirdClient.isDone) {
    source.take() match {
      case Some(json) => send(json)
      case None =>
    }
  }

  def send(msg: String): Unit = {
    val ts = System.currentTimeMillis()
    val payload = msg.map(_.toByte).toArray
    val record = new ProducerRecord[Array[Byte], Array[Byte]](topic, partition, ts, Array[Byte](), payload)
    producer.send(record)
  }
}
