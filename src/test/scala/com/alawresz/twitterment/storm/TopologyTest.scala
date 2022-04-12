package com.alawresz.twitterment.storm

import com.alawresz.twitterment.test.StormBaseTest
import com.alawresz.twitterment.configuration.Config
import com.alawresz.twitterment.storm.tw4jtopology.Tw4jTopology

import pureconfig.ConfigSource
import pureconfig.generic.auto._
import io.github.embeddedkafka.EmbeddedKafka

class StormConfigTest extends StormBaseTest with EmbeddedKafka {
  "Tw4JTopology" should {
    "successfully launch" in {
      withRunningKafka {
        withStormCluster { cluster =>
          val topology = Tw4jTopology
          run(topology, cluster)

          true shouldBe true
        }
      }
    }
  }
}