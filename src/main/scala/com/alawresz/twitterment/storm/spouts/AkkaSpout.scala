package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.web.Tweet
import com.alawresz.twitterment.web.AkkaRequest
import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.configuration.TwitterConfig

import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Values

import java.{util => ju}

class AkkaSpout(config: TwitterConfig) extends BaseRichSpout {
  var _collector: SpoutOutputCollector = _

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("key", "value"))

  override def open(conf: ju.Map[String,Object], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    _collector = collector
  }

  override def nextTuple(): Unit = {
    val request = AkkaRequest(
      config.bearerToken,
      config.url
    )
    val tweet = request.getTweets()
    println(tweet)
    _collector.emit(new Values("tweet", tweet.toString()))
  }
}

object AkkaSpout extends Configuration {
  def apply(): AkkaSpout =
    new AkkaSpout(config.twitterConfig)
}
