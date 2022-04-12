package com.alawresz.twitterment.test

import com.alawresz.twitterment.storm.BaseTopology
import com.alawresz.twitterment.configuration.Configuration

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import com.typesafe.scalalogging.LazyLogging
import org.apache.storm.LocalCluster
import scala.util.Using

trait BaseTest extends AnyWordSpec with Matchers with Configuration

trait StormBaseTest extends BaseTest {
  def run(topology: BaseTopology, cluster: LocalCluster): Unit = {
    cluster.submitTopology(stormConfig.topologyName, topology.topoConfig, topology.topology)
  }

  def withStormCluster[T](f: LocalCluster => T): T = {
    Using.resource(new LocalCluster()) { cluster =>
      f(cluster)  
    }
  }
}