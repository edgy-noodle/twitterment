package com.alawresz.twitterment.storm.akkatopology

import com.alawresz.twitterment.TweetModel._
import com.alawresz.twitterment.web.AkkaRequest
import com.alawresz.twitterment.configuration.{Configuration, TwitterAkkaConfig}

import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.tuple.{Fields, Values}

import java.{util => ju}
import scala.util.{Success, Failure}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AkkaSpout(config: TwitterAkkaConfig) extends BaseRichSpout with TweetSerialization {
  var _collector: SpoutOutputCollector  = _
  var _counter: Int                     = _

  private def emitTweet(tweetFuture: Future[Tweet]) = {
    tweetFuture.onComplete {
      case Failure(exception) =>
        logger.error(exception.toString())
      case Success(tweet) =>
        _collector.emit(new Values("tweet", serialize(tweet.data)), _counter)
    }
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("key", "value"))

  override def open(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: SpoutOutputCollector): Unit = {
      _collector  = collector
      _counter    = 20
    }

  override def nextTuple(): Unit = {
    val request = AkkaRequest(
      config.bearerToken,
      config.url + _counter
    )
    request.processTweetWith(emitTweet)
    Thread.sleep(100)
    _counter += 1
  }
}

object AkkaSpout extends Configuration {
  def apply(): AkkaSpout =
    new AkkaSpout(config.twitterConfig)
}
