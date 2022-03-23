package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.TweetModel.TweetSerialization

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import java.{util => ju}

class DeserializeTweetBolt extends IRichBolt with TweetSerialization {
  var _collector: OutputCollector     = _
  var _conf: ju.Map[String, Object]   = _

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      _collector  = collector
      _conf       = conf
    }

  override def execute(tuple: Tuple): Unit = {
    val value = tuple.getBinaryByField("value")
    deserialize(value) match {
      case Some(tweet) =>
        _collector.emit(tuple, new Values(tweet))
        _collector.ack(tuple)
      case None =>
        logger.error(s"Couldn't deserialize ${value}!")
        // No need to process if the value can't be deserialized
        _collector.ack(tuple)
    }
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("tweet"))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf
}

object DeserializeTweetBolt {
  def apply(): DeserializeTweetBolt =
    new DeserializeTweetBolt()
}