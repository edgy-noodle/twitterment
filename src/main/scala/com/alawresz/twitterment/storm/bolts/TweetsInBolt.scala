package com.alawresz.twitterment.storm.bolts
import com.alawresz.twitterment.web.Tweet
import org.apache.storm.kafka.bolt.KafkaBolt
import java.util.Properties
import org.apache.kafka.clients.producer.ProducerConfig


object TweetsInBolt {
  private val producerProps = (config: TweetSinkConfig) => {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers.mkString(","))
    props.put(ProducerConfig.CLIENT_ID_CONFIG, config.clientId)
    props.put(ProducerConfig.ACKS_CONFIG, config.acks)
    props.put(ProducerConfig.LINGER_MS_CONFIG, config.lingerMs)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.batchSize)
    props
  }

  def apply(config: TweetSinkConfig): KafkaBolt[String, Tweet] = {
    new KafkaBolt[String, Tweet]
      .withTopicSelector(config.topic)
      .withProducerProperties(producerProps(config))
  }
}