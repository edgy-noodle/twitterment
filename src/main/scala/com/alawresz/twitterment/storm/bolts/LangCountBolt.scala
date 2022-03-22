package com.alawresz.twitterment.storm.bolts

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import collection.{mutable => mt}
import java.{util => ju}

class LangCountBolt extends IRichBolt {
  var _collector: OutputCollector   = _
  var _conf: ju.Map[String, Object] = _
  var _counts: mt.Map[String, Int]  = _

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      _collector  = collector
      _conf       = conf
      _counts     = mt.Map[String, Int]().withDefaultValue(0)
    }

  override def execute(tuple: Tuple): Unit = {
    val lang = tuple.getStringByField("lang")
    _counts(lang) += 1
    println("-"*120)
    _counts.foreach(print)
    println("\n"+"-"*120)
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields())

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf
}

object LangCountBolt {
  def apply(): LangCountBolt =
    new LangCountBolt()
}