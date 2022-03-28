package com.alawresz.twitterment.helpers

import com.alawresz.twitterment.helpers.TweetModel._
import com.alawresz.twitterment.configuration.TwitterConfig

import com.typesafe.scalalogging.LazyLogging
import twitter4j.conf.ConfigurationBuilder
import twitter4j._

object Tw4jStream extends LazyLogging {
  private val twitterStreamConfig = (twitterConfig: TwitterConfig) => {
    val builder = new ConfigurationBuilder()
    builder.setOAuthConsumerKey(twitterConfig.consumerKey)
    builder.setOAuthConsumerSecret(twitterConfig.consumerSecret)
    builder.setOAuthAccessToken(twitterConfig.accessToken)
    builder.setOAuthAccessTokenSecret(twitterConfig.tokenSecret)
    builder.build()
  }

  private val listener = (tweetProducer: TweetProducer) => 
    new StatusListener() with TweetSerialization {
      override def onException(exception: Exception): Unit =
        logger.warn(exception.getMessage())
      override def onStatus(status: Status): Unit = {
        val tweet = TweetData(status.getId().toString(), status.getText())
        tweetProducer.sendTweet(serialize(tweet))
      }

      override def onDeletionNotice(arg0: StatusDeletionNotice): Unit = {}
      override def onTrackLimitationNotice(arg0: Int): Unit = {}
      override def onScrubGeo(arg0: Long, arg1: Long): Unit = {}
      override def onStallWarning(arg0: StallWarning): Unit = {}
    }

  def apply(config: TwitterConfig, tweetProducer: TweetProducer): TwitterStream = {
    val stream = new TwitterStreamFactory(twitterStreamConfig(config)).getInstance()
    stream.addListener(listener(tweetProducer))
    
    logger.info("Tw4jStream: Obtained a stream sample from the listener.")
    stream.sample()
  }
}