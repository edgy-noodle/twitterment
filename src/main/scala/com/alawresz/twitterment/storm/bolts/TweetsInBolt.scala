package com.alawresz.twitterment.storm.bolts
import com.alawresz.twitterment.web.Tweet
import org.apache.storm.kafka.bolt.KafkaBolt
import java.util.Properties


object TweetsInBolt {
  private val producerProps = (config: TweetSinkConfig) => {
    val props = new Properties()
    props
  }

  def apply(config: TweetSinkConfig): KafkaBolt[String, Tweet] = {
    new KafkaBolt[String, Tweet]
      .withProducerProperties(producerProps(config))
  }
}
