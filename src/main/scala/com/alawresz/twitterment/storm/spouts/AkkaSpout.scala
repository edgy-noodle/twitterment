package com.alawresz.twitterment.storm.spouts

import com.alawresz.twitterment.web.AkkaRequest
import com.alawresz.twitterment.configuration.{Configuration, TwitterConfig}

import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.tuple.{Fields, Values}

import com.typesafe.scalalogging.LazyLogging
import java.{util => ju}
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

class AkkaSpout(config: TwitterConfig) extends BaseRichSpout with LazyLogging {
  var _collector: SpoutOutputCollector  = _
  var _counter: Int                     = _

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("key", "value"))

  override def open(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: SpoutOutputCollector): Unit = {
      _collector = collector
      _counter = 20
    }

  override def nextTuple(): Unit = {
    val request = AkkaRequest(
      config.bearerToken,
      config.url + _counter
    )
    val tweetFuture = request.getTweet()
    tweetFuture.onComplete {
      case Failure(exception) =>
        logger.error(exception.toString())
      case Success(tweet) =>
        _collector.emit(new Values("tweet", tweet.data.serialized), _counter)
    }
    Thread.sleep(3000)
    _counter += 1
  }
}

object AkkaSpout extends Configuration {
  def apply(): AkkaSpout =
    new AkkaSpout(config.twitterConfig)
}
