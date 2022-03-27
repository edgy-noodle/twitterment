package com.alawresz.twitterment.storm.akkatopology

import com.alawresz.twitterment.configuration.ProdConfig
import com.alawresz.twitterment.helpers.TweetModel.Tweet
import com.alawresz.twitterment.storm.TupleModel

import org.apache.storm.kafka.bolt.KafkaBolt
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{StringSerializer, ByteArraySerializer}

import java.util.Properties

object TweetsInBolt {
  private val producerProps = (config: ProdConfig) => {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, config.clientId)
    props.put(ProducerConfig.ACKS_CONFIG, config.acks)
    props.put(ProducerConfig.LINGER_MS_CONFIG, config.lingerMs)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.batchSize)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[ByteArraySerializer])
    props
  }
  private val fieldsMapper = new FieldNameBasedTupleToKafkaMapper[String, Array[Byte]](
    TupleModel.key, TupleModel.value
  )

  def apply(config: ProdConfig): KafkaBolt[String, Array[Byte]] = {
    new KafkaBolt[String, Array[Byte]]
      .withTopicSelector(config.topic)
      .withProducerProperties(producerProps(config))
      .withTupleToKafkaMapper(fieldsMapper)
  }
}