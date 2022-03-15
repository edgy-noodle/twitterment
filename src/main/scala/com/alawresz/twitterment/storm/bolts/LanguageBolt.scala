package com.alawresz.twitterment.storm.bolts

import org.apache.storm.topology.IRichBolt
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.Tuple
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.tuple.Fields

import java.{util => ju}

class LanguageBolt extends IRichBolt {
  override def prepare(x$1: ju.Map[String,Object], x$2: TopologyContext, x$3: OutputCollector): Unit = ???

  override def execute(tuple: Tuple): Unit = {

  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("tweet", "lang"))

  override def getComponentConfiguration(): ju.Map[String,Object] = {
    ???
  }
}