package com.alawresz.twitterment.storm

import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.bolts._
import com.alawresz.twitterment.storm.TupleModel

import org.apache.storm.{Config, LocalCluster, StormSubmitter}
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.tuple.{Fields, Values}
import org.apache.storm.metrics2.reporters.GraphiteStormReporter

trait BaseTopology extends Configuration {
  val builder                     = new TopologyBuilder()
  private lazy val topology       = {
    getTweets()
    getLanguages()
    getSentiments()
    storeResults()

    builder.createTopology()
  }
  private lazy val metricsConfig  = {
    import scala.collection.JavaConverters._
    
    List(
      Map[String, AnyRef](
        ("class", classOf[GraphiteStormReporter].toString().stripPrefix("class ")),
        (GraphiteStormReporter.GRAPHITE_HOST, graphiteConfig.hostAddress),
        (GraphiteStormReporter.GRAPHITE_PORT, graphiteConfig.hostPort.toString())
      ).asJava
    ).asJava
  }
  private lazy val clusterConfig  = {
    val config = new Config()
    config.setNumWorkers(stormConfig.numWorkers)
    config.setDebug(stormConfig.debug)
    config.setFallBackOnJavaSerialization(stormConfig.javaSerialization)
    config.setMaxSpoutPending(stormConfig.maxSpoutPending)
    config.setMessageTimeoutSecs(stormConfig.messageTimeout)

    config.put(Config.TOPOLOGY_METRICS_REPORTERS, metricsConfig)
    config
  }

  private def getTweets(): Unit = {
    val tweetsInSpout = TweetsInSpout(kafkaConfig.consumer)
    builder
      .setSpout("tweetsInSpout", tweetsInSpout, 1)
    logger.info(s"Consuming from ${kafkaConfig.consumer.topic} topic")

    val deserializeTweetBolt = DeserializeTweetBolt()
    builder
      .setBolt("deserializeTweetBolt", deserializeTweetBolt, 1)
      .shuffleGrouping("tweetsInSpout")
  }

  private def getLanguages(): Unit = {
    val detectLangBolt = DetectLangBolt()
    builder
      .setBolt("detectLangBolt", detectLangBolt, 1)
      .shuffleGrouping("deserializeTweetBolt")
  }

  private def getSentiments(): Unit = {
    val detectSentimentBolt = DetectSentimentBolt()
    builder
      .setBolt("detectSentimentBolt", detectSentimentBolt, 20)
      .shuffleGrouping("detectLangBolt")
  }

  private def storeResults(): Unit = {
    val langStoreBolt = SaveHashToRedisBolt(redisConfig.keys.langKey, TupleModel.lang)
    builder
      .setBolt("langStoreBolt", langStoreBolt, 1)
      .shuffleGrouping("detectLangBolt")
    
    val sentimentStoreBolt = SaveHashToRedisBolt(redisConfig.keys.sentimentKey, TupleModel.sentiment)
    builder
      .setBolt("sentimentStoreBolt", sentimentStoreBolt, 20)
      .shuffleGrouping("detectSentimentBolt")
  }

  def startLocalTopology(): Unit = {
    val cluster = new LocalCluster()
    cluster.submitTopology(stormConfig.topologyName, clusterConfig, topology)
  }

  def startRemoteTopology(): Unit =
    StormSubmitter.submitTopologyWithProgressBar(stormConfig.topologyName, clusterConfig, topology)
}