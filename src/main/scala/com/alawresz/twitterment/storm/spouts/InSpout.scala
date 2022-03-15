package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.web.Tweet
import org.apache.storm.kafka.spout.KafkaSpout
import org.apache.storm.kafka.spout.KafkaSpoutConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

object TweetSpout {
  private val kafkaSpoutConfig = (config: InSpoutConfig) =>
    new KafkaSpoutConfig
      .Builder[String, Tweet](config.bootstrapServers.mkString(","), config.topic)
      .setProcessingGuarantee(KafkaSpoutConfig.ProcessingGuarantee.AT_MOST_ONCE)
      .setProp(ConsumerConfig.GROUP_ID_CONFIG, config.groupId)
      .setProp(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, config.fetchMinBytes)
      .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
      .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
      .build()

  def apply(config: InSpoutConfig): KafkaSpout[String, Tweet] = {
    new KafkaSpout[String, Tweet](kafkaSpoutConfig(config))
  }
}