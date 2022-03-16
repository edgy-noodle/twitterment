package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.TweetModel.TweetData

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

  private val listener = (tweetProducer: TweetData => Unit) => 
    new StatusListener() with LazyLogging {
      override def onException(exception: Exception): Unit =
        logger.error(exception.toString())
      override def onStatus(status: Status): Unit = {
        val tweet = TweetData(status.getId().toString(), status.getText())
        tweetProducer(tweet)
      }

      override def onDeletionNotice(arg0: StatusDeletionNotice): Unit = {}
      override def onTrackLimitationNotice(arg0: Int): Unit = {}
      override def onScrubGeo(arg0: Long, arg1: Long): Unit = {}
      override def onStallWarning(arg0: StallWarning): Unit = {}
    }

  def apply(config: TwitterConfig, tweetProducer: TweetData => Unit): TwitterStream = {
    val stream = new TwitterStreamFactory(twitterStreamConfig(config)).getInstance()
    stream.addListener(listener(tweetProducer))
  }
}