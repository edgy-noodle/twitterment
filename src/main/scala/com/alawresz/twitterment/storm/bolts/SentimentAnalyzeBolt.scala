package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.nlp.SentimentAnalyzer
import com.alawresz.twitterment.TweetModel.TweetData

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import java.{util => ju}

class SentimentAnalyzeBolt extends IRichBolt {
  var _collector: OutputCollector   = _
  var _conf: ju.Map[String, Object] = _
  var _analyzer: SentimentAnalyzer  = _

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      _collector  = collector
      _conf       = conf 
      _analyzer   = SentimentAnalyzer()
    }

  override def execute(tuple: Tuple): Unit = {
    val tweet     = tuple.getValueByField("tweet").asInstanceOf[TweetData]
    val sentiment = _analyzer.findSentiment(tweet.text)
    _collector.emit(tuple, new Values(tweet, sentiment))
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("tweet", "sentiment"))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf  
}

object SentimentAnalyzeBolt {
  def apply(): SentimentAnalyzeBolt =
    new SentimentAnalyzeBolt()
}