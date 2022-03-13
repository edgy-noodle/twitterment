package com.alawresz.twitterment.storm

import com.alawresz.twitterment.Test
import com.alawresz.twitterment.configuration.Config

import pureconfig.ConfigSource
import pureconfig.generic.auto._

class StormConfigTest extends Test {
  val config = ConfigSource.default.loadOrThrow[Config]
  "This" should {
    "work" in {
      true shouldBe true
    }
  }
}