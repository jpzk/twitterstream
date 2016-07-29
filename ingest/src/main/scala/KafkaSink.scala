package mwt.twitterstreams

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Kafka sink to send raw messages to a topic
  *
  * @param brokers
  * @param topic
  * @param partition
  * @param key
  */
class KafkaSink(brokers: String, topic: String, partition: Int, key: String) extends Logging {

  private final val keyBytes = key.map(_.toByte).toArray
  private final val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("acks", "all")
  props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")

  private final val producer = new KafkaProducer[Array[Byte], Array[Byte]](props)

  def send(msg: String): Unit = {
    val ts = System.currentTimeMillis()
    val payload = msg.map(_.toByte).toArray
    val record = new ProducerRecord[Array[Byte], Array[Byte]](topic, partition, ts, keyBytes, payload)
    log.info(s"Sending record: $msg")
    producer.send(record)
  }

  def close = producer.close()
}
