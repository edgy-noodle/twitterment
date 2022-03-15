package com.alawresz.twitterment.storm

import com.alawresz.twitterment.storm.spouts._
import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts.TweetsInBolt

import org.apache.storm.Config
import org.apache.storm.LocalCluster
import org.apache.storm.generated.StormTopology
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Values

object Topology extends Configuration {
  def buildTopology(): StormTopology = {
    val builder = new TopologyBuilder()

    val akkaSpout = AkkaSpout()
    builder
      .setSpout("akkaSpout", akkaSpout)
    val inBolt = TweetsInBolt(stormConfig.inBolt)
    builder
      .setBolt("inBolt", inBolt)
      .shuffleGrouping("akkaSpout")

    val inSpout = TweetSpout(stormConfig.inSpout)
    builder
      .setSpout("inSpout", inSpout)

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