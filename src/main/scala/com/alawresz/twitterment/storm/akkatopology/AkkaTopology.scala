package com.alawresz.twitterment.storm.akkatopology

import com.alawresz.twitterment.storm.BaseTopology
import com.alawresz.twitterment.storm.bolts._

import org.apache.storm.{Config, LocalCluster}
import org.apache.storm.generated.StormTopology
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.{Fields, Values}

object AkkaTopology extends BaseTopology {
  private def produceTweets(): Unit = {
    val akkaSpout = AkkaSpout()
    builder
      .setSpout("akkaSpout", akkaSpout)

    // Implemented just for learning purposes
    val tweetsTweetsInBolt = TweetsInBolt(kafkaConfig.producer)
    builder
      .setBolt("tweetsTweetsInBolt", tweetsTweetsInBolt)
      .shuffleGrouping("akkaSpout")

    logger.info(s"Produding tweets to ${kafkaConfig.producer.topic} topic")
  }

  def apply(isLocal: Boolean): Unit = {
    produceTweets()
    if (isLocal) startLocalTopology()
    else startRemoteTopology()
  }
}