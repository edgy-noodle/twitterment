package com.alawresz.twitterment.storm.tw4jtopology

import com.alawresz.twitterment.helpers.TweetProducer
import com.alawresz.twitterment.storm.BaseTopology
import com.alawresz.twitterment.helpers.Tw4jStream

import org.apache.storm.LocalCluster

object Tw4jTopology extends BaseTopology {
  private def produceTweets(): Unit = {
    val tweetProducer = TweetProducer()
    val tw4jStream = Tw4jStream(twitterConfig, tweetProducer)
  }

  def apply(isLocal: Boolean = false): Unit = {
    produceTweets()
    if (isLocal) startLocalTopology()
    else startRemoteTopology()
  }
}