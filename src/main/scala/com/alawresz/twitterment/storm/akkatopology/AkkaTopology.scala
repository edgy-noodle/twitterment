package com.alawresz.twitterment.storm.akkatopology

import com.alawresz.twitterment.storm.spouts._
import com.alawresz.twitterment.storm.bolts._
import com.alawresz.twitterment.configuration.Configuration

import org.apache.storm.{Config, LocalCluster}
import org.apache.storm.generated.StormTopology
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.{Fields, Values}

object AkkaTopology extends Configuration {
  private val builder = new TopologyBuilder()

  private def produceTweets(): Unit = {
    val akkaSpout = AkkaSpout()
    builder
      .setSpout("akkaSpout", akkaSpout)

    // Implemented just for learning purposes
    val inBolt = TweetsInBolt(kafkaConfig.producer)
    builder
      .setBolt("inBolt", inBolt)
      .shuffleGrouping("akkaSpout")

    logger.info(s"Produding tweets to ${kafkaConfig.producer.topic} topic")
  }

  private def getLanguages(): Unit = {
    val langDetectBolt = LangDetectBolt()
    builder
      .setBolt("langDetectBolt", langDetectBolt)
      .shuffleGrouping("inSpout")

    val langCountBolt = LangCountBolt()
    builder
      .setBolt("langCountBolt", langCountBolt)
      .fieldsGrouping("langDetectBolt", new Fields("lang"))
  }

  def buildTopology(): StormTopology = {
    produceTweets()

    val inSpout = InSpout(kafkaConfig.consumer)
    builder
      .setSpout("inSpout", inSpout)
    logger.info(s"Consuming from ${kafkaConfig.consumer.topic} topic")

    getLanguages()

    builder.createTopology()
  }
  
  def startTopology(): Unit = {
    val cluster = new LocalCluster("localhost", 2181)
    val clusterConfig = new Config()
    val topology = buildTopology()
    // clusterConfig.setDebug(true)
    cluster.submitTopology("twitterment", clusterConfig, topology)
  }
}