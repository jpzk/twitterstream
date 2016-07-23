package mwt.twitterstreams

import java.io.File

import com.twitter.hbc.httpclient.auth.OAuth1
import com.typesafe.config.ConfigFactory

object Settings {

  val config = ConfigFactory.parseFile(new File("application.conf"))
  val kConfig = config.getConfig("kafka")
  val tConfig = config.getConfig("twitter")

  /**
    * Return filter terms specified in config
    *
    * @return
    */
  def getFilterTerms = {
    val terms = tConfig.getStringList("terms")
    Range(0, terms.size()).map { i => terms.get(i) }
  }

  /**
    * Return instance of Kafka sink
    *
    * @return
    */
  def getKafkaSink = {
    val brokers = kConfig.getString("brokers")
    val topic = kConfig.getString("topic")
    val partition = kConfig.getInt("partition")
    val key = getFilterTerms.mkString(",")

    new KafkaSink(brokers, topic, partition, key)
  }

  /**
    * Return instance of hosebird msg source
    *
    * @return
    */
  def getHosebirdMsgSource = {
    val oAuth1 = new OAuth1(
      tConfig.getString("consumer_key"),
      tConfig.getString("consumer_secret"),
      tConfig.getString("token"),
      tConfig.getString("token_secret"))

    new HosebirdMsgSource(oAuth1, getFilterTerms)
  }
}
