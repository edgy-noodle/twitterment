package com.alawresz.twitterment.storm

import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts._
import com.alawresz.twitterment.storm.TupleModel

import org.apache.storm.{Config, LocalCluster}
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.{Fields, Values}

trait BaseTopology extends Configuration {
  val builder = new TopologyBuilder()

  private def getTweets(): Unit = {
    val inSpout = InSpout(kafkaConfig.consumer)
    builder
      .setSpout("inSpout", inSpout, 1)
    logger.info(s"Consuming from ${kafkaConfig.consumer.topic} topic")

    val deserializeTweetBolt = DeserializeTweetBolt()
    builder
      .setBolt("deserializeTweetBolt", deserializeTweetBolt, 1)
      .shuffleGrouping("inSpout")
  }

  private def getLanguages(): Unit = {
    val langDetectBolt = LangDetectBolt()
    builder
      .setBolt("langDetectBolt", langDetectBolt, 1)
      .shuffleGrouping("deserializeTweetBolt")

    
  }

  private def getSentiments(): Unit = {
    val sentimentAnalyzeBolt = SentimentAnalyzeBolt()
    builder
      .setBolt("sentimentAnalyzeBolt", sentimentAnalyzeBolt, 20)
      .shuffleGrouping("langDetectBolt")
  }

  private def storeResults(): Unit = {
    val langStoreBolt = RedisSaveBolt(redisConfig.keys.langKey, TupleModel.lang)
    builder
      .setBolt("langStoreBolt", langStoreBolt, 1)
      .shuffleGrouping("langDetectBolt")
    
    val sentimentStoreBolt = RedisSaveBolt(redisConfig.keys.sentimentKey, TupleModel.sentiment)
    builder
      .setBolt("sentimentStoreBolt", sentimentStoreBolt, 20)
      .shuffleGrouping("sentimentAnalyzeBolt")
  }

  def startTopology(name: String): Unit = {
    val cluster = new LocalCluster()
    val clusterConfig = new Config()
    clusterConfig.setDebug(true)

    val topology = {
      getTweets()
      getLanguages()
      getSentiments()
      storeResults()
      
      builder.createTopology()
    }
    cluster.submitTopology("twitterment", clusterConfig, topology)
  }
}
