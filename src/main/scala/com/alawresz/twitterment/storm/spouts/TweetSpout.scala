package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.web.Tweet
import org.apache.storm.kafka.spout.KafkaSpout
import org.apache.storm.kafka.spout.KafkaSpoutConfig

object TweetSpout {
  private val kafkaSpoutConfig = (config: TweetSpoutConfig) =>
    new KafkaSpoutConfig
      .Builder[String, Tweet](config.bootstrapServers.mkString(","), config.topic)
      .setProcessingGuarantee(KafkaSpoutConfig.ProcessingGuarantee.AT_MOST_ONCE)
      .build()

  def apply(config: TweetSpoutConfig): KafkaSpout[String, Tweet] = {
    new KafkaSpout[String, Tweet](kafkaSpoutConfig(config))
  }

}