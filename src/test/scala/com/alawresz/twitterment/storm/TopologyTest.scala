package com.alawresz.twitterment.storm

import com.alawresz.twitterment.TestBase
import io.github.embeddedkafka.EmbeddedKafka

class TopologyTest extends TestBase with EmbeddedKafka {
  "This" should {
    "work" in {
      withRunningKafka {
        true shouldBe true
      }
    }
  }
}