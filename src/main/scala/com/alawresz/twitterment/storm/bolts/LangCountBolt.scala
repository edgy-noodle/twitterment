package com.alawresz.twitterment.storm.bolts

import org.apache.storm.topology.IRichBolt
import java.{util => ju}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.Tuple
import org.apache.storm.topology.OutputFieldsDeclarer
import java.{util => ju}
import org.apache.storm.tuple.Fields

class LangCountBolt extends IRichBolt {
  var _collector: OutputCollector   = _
  var _conf: ju.Map[String, Object] = _

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      _collector = collector
      _conf = conf
    }

  override def execute(tuple: Tuple): Unit = {

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