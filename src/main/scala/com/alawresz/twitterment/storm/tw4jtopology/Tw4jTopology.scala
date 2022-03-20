package com.alawresz.twitterment.storm.tw4jtopology

import com.alawresz.twitterment.storm.BaseTopology
import com.alawresz.twitterment.TweetProducer
import com.alawresz.twitterment.TweetStream
import com.alawresz.twitterment.configuration.TwitterConfig

object Tw4jTopology extends BaseTopology {
  private def produceTweets(twitterConfig: TwitterConfig): Unit = {
    val tweetProducer = TweetProducer()
    val tweetStream = TweetStream(twitterConfig, tweetProducer)
  }

  def apply(twitterConfig: TwitterConfig): Unit = {
    produceTweets(twitterConfig)
    startTopology("Tw4jTopology")
  }
}
