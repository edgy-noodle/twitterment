package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.storm.akkatopology.AkkaTopology
import com.alawresz.twitterment.storm.tw4jtopology.Tw4jTopology

object App extends Configuration {
  def main(args: Array[String]): Unit = {
    if(args.length == 0) {
      printAkkaConfig()
      AkkaTopology()
    } else {
      val twitterConfig = TwitterConfig(args(0), args(1), args(2), args(3))
      printTw4jConfig(twitterConfig)
      Tw4jTopology(twitterConfig)
    }
  }
}