package com.alawresz.twitterment.storm

import org.apache.storm.LocalCluster
import org.apache.storm.generated.StormTopology
import org.apache.storm.Config
import org.apache.storm.topology.TopologyBuilder
import com.alawresz.twitterment.storm.spouts.TweetSpout
import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts.TweetsInBolt

object Topology extends Configuration {
  def buildTopology(): StormTopology = {
    val builder = new TopologyBuilder()

    val tweetsInBolt = TweetsInBolt(config.stormConfig.tweetsIn)
    builder
      .setBolt(tweetsInBolt.toString(), tweetsInBolt)
    // val tweetSpout = TweetSpout(config.stormConfig.tweetsSpout)
    // builder
    //   .setSpout(tweetSpout.toString(), tweetSpout)

    builder.createTopology()
  }
  
  def startTopology(): Unit = {
    val cluster = new LocalCluster()
    val clusterConfig = new Config()
    val topology = buildTopology()
    clusterConfig.setDebug(true)
    cluster.submitTopology(topology.toString(), clusterConfig, topology)
  }
}