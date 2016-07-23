package mwt.twitterstreams

import java.util.concurrent.LinkedBlockingQueue

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.{Constants, HttpHosts}
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.auth.OAuth1
import java.util.ArrayList

/**
  * Source for status messages filtered by terms
  *
  * @param oAuth1
  * @param terms
  */
class HosebirdMsgSource(oAuth1: OAuth1, terms: Seq[String]) extends Logging {
  val msgQueue = new LinkedBlockingQueue[String](10)

  val hosebirdEndpoint = new StatusesFilterEndpoint()
  val listOfTerms = new ArrayList[String]()
  terms.foreach { term => listOfTerms.add(term) }
  hosebirdEndpoint.trackTerms(listOfTerms)

  val builder = new ClientBuilder()
    .hosts(new HttpHosts(Constants.STREAM_HOST))
    .authentication(oAuth1)
    .endpoint(hosebirdEndpoint)
    .processor(new StringDelimitedProcessor(msgQueue))

  val hosebirdClient = builder.build()
  hosebirdClient.connect()

  def take(): Option[String] = {
    if (hosebirdClient.isDone)
      None
    else
      Some(msgQueue.take())
  }
}
