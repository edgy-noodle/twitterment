package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.TweetModel._

import com.typesafe.scalalogging.LazyLogging
import twitter4j.conf.ConfigurationBuilder
import twitter4j._

object TweetStream {
  private val twitterStreamConfig = (twitterConfig: TwitterConfig) => {
    val builder = new ConfigurationBuilder()
    builder.setOAuthConsumerKey(twitterConfig.consumerKey)
    builder.setOAuthConsumerSecret(twitterConfig.consumerSecret)
    builder.setOAuthAccessToken(twitterConfig.accessToken)
    builder.setOAuthAccessTokenSecret(twitterConfig.tokenSecret)
    builder.build()
  }

  private val listener = (tweetProducer: Array[Byte] => Unit) => 
    new StatusListener() with TweetSerialization {
      override def onException(exception: Exception): Unit =
        logger.error(exception.toString())
      override def onStatus(status: Status): Unit = {
        val tweet = TweetData(status.getId().toString(), status.getText())
        tweetProducer(serialize(tweet))
      }

      override def onDeletionNotice(arg0: StatusDeletionNotice): Unit = {}
      override def onTrackLimitationNotice(arg0: Int): Unit = {}
      override def onScrubGeo(arg0: Long, arg1: Long): Unit = {}
      override def onStallWarning(arg0: StallWarning): Unit = {}
    }

  def apply(config: TwitterConfig, tweetProducer: Array[Byte] => Unit): TwitterStream = {
    val stream = new TwitterStreamFactory(twitterStreamConfig(config)).getInstance()
    stream.addListener(listener(tweetProducer))
    stream.sample()
  }
}