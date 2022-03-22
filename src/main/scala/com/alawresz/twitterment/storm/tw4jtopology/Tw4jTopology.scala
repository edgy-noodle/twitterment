package com.alawresz.twitterment.storm.tw4jtopology

import com.alawresz.twitterment.TweetProducer
import com.alawresz.twitterment.storm.BaseTopology
import com.alawresz.twitterment.tw4j.TweetStream

object Tw4jTopology extends BaseTopology {
  private def produceTweets(): Unit = {
    val tweetProducer = TweetProducer()
    val tweetStream = TweetStream(twitterConfig, tweetProducer)
  }

  def apply(): Unit = {
    produceTweets()
    startTopology("Tw4jTopology")
  }
}