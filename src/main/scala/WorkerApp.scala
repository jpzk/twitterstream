package mwt.twitterstreams

import com.twitter.util.Time
import com.twitter.conversions.time._

/**
  * Worker app
  */
object WorkerApp extends App with Logging {
  log.info(Settings.config.toString)

  val source = Settings.getHosebirdMsgSource
  val sink = Settings.getKafkaSink

  while(!source.hosebirdClient.isDone) {
    source.take() match {
      case Some(json) => sink.send(json)
      case None =>
    }
    Time.sleep(1.second)
  }

}
