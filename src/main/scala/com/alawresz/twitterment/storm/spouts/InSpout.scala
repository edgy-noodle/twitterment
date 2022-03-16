package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.TweetModel.Tweet

import org.apache.storm.kafka.spout.{KafkaSpout, KafkaSpoutConfig}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.ByteArrayDeserializer

object InSpout {
  private val kafkaSpoutConfig = (config: InSpoutConfig) =>
    new KafkaSpoutConfig
      .Builder[String, Tweet](config.bootstrapServers.mkString(","), config.topic)
      .setProcessingGuarantee(KafkaSpoutConfig.ProcessingGuarantee.AT_MOST_ONCE)
      .setProp(ConsumerConfig.GROUP_ID_CONFIG, config.groupId)
      .setProp(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, config.fetchMinBytes)
      .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
      .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[ByteArrayDeserializer])
      .build()

  def apply(config: InSpoutConfig): KafkaSpout[String, Tweet] = {
    new KafkaSpout[String, Tweet](kafkaSpoutConfig(config))
  }
}