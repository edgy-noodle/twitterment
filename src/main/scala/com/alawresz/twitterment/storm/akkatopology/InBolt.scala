package com.alawresz.twitterment.storm.akkatopology

import com.alawresz.twitterment.configuration.ProdConfig
import com.alawresz.twitterment.TweetModel.Tweet

import org.apache.storm.kafka.bolt.KafkaBolt
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{StringSerializer, ByteArraySerializer}

import java.util.Properties

object InBolt {
  private val producerProps = (config: ProdConfig) => {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers.mkString(","))
    props.put(ProducerConfig.CLIENT_ID_CONFIG, config.clientId)
    props.put(ProducerConfig.ACKS_CONFIG, config.acks)
    props.put(ProducerConfig.LINGER_MS_CONFIG, config.lingerMs)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.batchSize)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[ByteArraySerializer])
    props
  }

  def apply(config: ProdConfig): KafkaBolt[String, String] = {
    new KafkaBolt[String, String]
      .withTopicSelector(config.topic)
      .withProducerProperties(producerProps(config))
      .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("key", "value"))
  }
}