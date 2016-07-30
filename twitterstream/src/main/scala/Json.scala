package mwt.twitterstream

import java.lang.reflect.{ParameterizedType, Type}
import java.nio.charset.StandardCharsets.UTF_8

import com.fasterxml.jackson.annotation.{JsonFilter, JsonInclude}
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.{UnrecognizedPropertyException ⇒ UPE}
import com.fasterxml.jackson.databind.ser.impl.{SimpleBeanPropertyFilter, SimpleFilterProvider}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.io.{Buf => TwitterBuf}
import org.jboss.netty.buffer.{ChannelBuffer, ChannelBuffers}

object Json {

  type ParseException = JsonParseException
  type UnrecognizedPropertyException = UPE

  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

  private def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {
      m.runtimeClass
    }
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

  object String {

    import scala.collection.JavaConversions._

    @JsonFilter("ignored fields")
    class PropertyFilterMixIn

    private val filterMapper = new ObjectMapper()
      .registerModule(DefaultScalaModule)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .addMixIn(classOf[Any], classOf[PropertyFilterMixIn])

    def encode(value: Any): String =
      mapper.writeValueAsString(value)


    def encode(value: Any, ignore: Set[String]): String = {
      val filterProv: SimpleFilterProvider = {
        val filter = SimpleBeanPropertyFilter.serializeAllExcept(ignore)
        new SimpleFilterProvider().addFilter("ignored fields", filter)
      }

      filterMapper.writer(filterProv).writeValueAsString(value)
    }

    def decode[T: Manifest](value: String): T =
      filterMapper.readValue(value, typeReference[T])
  }


  object Buf {
    def encode(value: Any): TwitterBuf =
      TwitterBuf.Utf8(mapper.writeValueAsString(value))

    def decode[T: Manifest](value: TwitterBuf): T = value match {
      case TwitterBuf.Utf8(resp) =>
        mapper.readValue(resp, typeReference[T])
    }
  }

  object ByteArray {
    def decode[T: Manifest](value: Array[Byte]): T =
      mapper.readValue(value, typeReference[T])
  }

  object ChannelBuffer {
    def encode(value: Any): ChannelBuffer =
      ChannelBuffers.wrappedBuffer(mapper.writeValueAsBytes(value))

    def decode[T: Manifest](value: ChannelBuffer): T =
      mapper.readValue(value.toString(UTF_8), typeReference[T])
  }

}
