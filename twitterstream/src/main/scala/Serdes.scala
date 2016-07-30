package mwt.twitterstream

import com.twitter.io.Buf
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import java.util

/**
  * JSON serializer for JSON serde
  *
  * @tparam T
  */
class JSONSerializer[T] extends Serializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] = {
    Buf.ByteArray.Owned.extract(Json.Buf.encode(data))
  }

  override def close(): Unit = ()
}

/**
  * JSON deserializer for JSON serde
  *
  * @tparam T
  */
class JSONDeserializer[T >: Null <: Any : Manifest] extends Deserializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T = {
    if (data == null) {
      return null
    } else {
      Json.Buf.decode[T](Buf.ByteArray.Owned(data))
    }
  }
}

/**
  * JSON serde for local state serialization
  *
  * @tparam T
  */
class JSONSerde[T >: Null <: Any : Manifest] extends Serde[T] {
  override def deserializer(): Deserializer[T] = new JSONDeserializer[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def serializer(): Serializer[T] = new JSONSerializer[T]
}

