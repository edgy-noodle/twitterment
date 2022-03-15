package com.alawresz.twitterment.storm

import org.apache.storm.LocalCluster
import org.apache.storm.generated.StormTopology
import org.apache.storm.Config
import org.apache.storm.topology.TopologyBuilder
import com.alawresz.twitterment.storm.spouts._
import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts.TweetsInBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Values

object Topology extends Configuration {
  def buildTopology(): StormTopology = {
    val builder = new TopologyBuilder()

    val akkaSpout = AkkaSpout()
    builder
      .setSpout("akkaSpout", akkaSpout)

    val tweetsInBolt = TweetsInBolt(config.stormConfig.tweetsIn)
    builder
      .setBolt("tweetsInBolt", tweetsInBolt)
      .shuffleGrouping("akkaSpout")
    // val tweetSpout = TweetSpout(config.stormConfig.tweetsSpout)
    // builder
    //   .setSpout(tweetSpout.toString(), tweetSpout)

    builder.createTopology()
  }
  
  def startTopology(): Unit = {
    val cluster = new LocalCluster()
    val clusterConfig = new Config()
    val topology = buildTopology()
    // clusterConfig.setDebug(true)
    cluster.submitTopology("twitterment", clusterConfig, topology)
  }
}