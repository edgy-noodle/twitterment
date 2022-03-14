package com.alawresz.twitterment.storm

import org.apache.storm.LocalCluster
import org.apache.storm.generated.StormTopology
import org.apache.storm.Config

object Topology {
  def startTopology(topology: StormTopology, conf: Config): Unit = {
    val cluster = new LocalCluster()
    cluster.submitTopology(topology.toString(), conf, topology)
  }

  def startSpout(): Unit = {

  }
}
