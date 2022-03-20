package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.ProdConfig
import com.alawresz.twitterment.configuration.Configuration

import java.util.Properties

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata

class TweetProducer(props: Properties, topic: String) {
  private val producer = new KafkaProducer[String, Array[Byte]](props)

  def sendTweet(tweet: Array[Byte]): Unit = {
    val message = new ProducerRecord[String, Array[Byte]](
      topic, "tweet", tweet
    )
    producer.send(message, TweetProducer.ProducerCallback)
  }
}

object TweetProducer extends Configuration {
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

  def apply(): TweetProducer = {
    val config = kafkaConfig.producer
    new TweetProducer(producerProps(config), config.topic)
  }

  private object ProducerCallback extends Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
      if (exception != null) logger.error(exception.toString())
    }
  }
}