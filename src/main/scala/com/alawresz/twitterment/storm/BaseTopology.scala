package com.alawresz.twitterment.storm

import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts._

import org.apache.storm.{Config, LocalCluster}
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.{Fields, Values}

trait BaseTopology extends Configuration {
  val builder = new TopologyBuilder()

  private def getTweets(): Unit = {
    val inSpout = InSpout(kafkaConfig.consumer)
    builder
      .setSpout("inSpout", inSpout)
    logger.info(s"Consuming from ${kafkaConfig.consumer.topic} topic")

    val deserializeTweetBolt = DeserializeTweetBolt()
    builder
      .setBolt("deserializeTweetBolt", deserializeTweetBolt)
      .shuffleGrouping("inSpout")
  }

  private def getLanguages(): Unit = {
    val langDetectBolt = LangDetectBolt()
    builder
      .setBolt("langDetectBolt", langDetectBolt)
      .shuffleGrouping("deserializeTweetBolt")

    val langCountBolt = LangCountBolt()
    builder
      .setBolt("langCountBolt", langCountBolt)
      .fieldsGrouping("langDetectBolt", new Fields("lang"))
  }

  private def getSentiments(): Unit = {
    val sentimentAnalyzeBolt = SentimentAnalyzeBolt()
    builder
      .setBolt("sentimentAnalyzeBolt", sentimentAnalyzeBolt)
      .shuffleGrouping("deserializeTweetBolt")
  }

  def startTopology(name: String): Unit = {
    val cluster = new LocalCluster()
    val clusterConfig = new Config()
    // clusterConfig.setDebug(true)

    val topology = {
      getTweets()
      getLanguages()
      
      builder.createTopology()
    }
    cluster.submitTopology("twitterment", clusterConfig, topology)
  }
}
