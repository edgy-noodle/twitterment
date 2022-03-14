package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.web.Tweet
import org.apache.storm.kafka.spout.KafkaSpout

object TweetSpout {
  def apply(): KafkaSpout[String, Tweet] = {
    ???
  }
}
