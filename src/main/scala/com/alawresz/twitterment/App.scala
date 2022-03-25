package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.{Configuration, TwitterConfig}
import com.alawresz.twitterment.storm.akkatopology.AkkaTopology
import com.alawresz.twitterment.storm.tw4jtopology.Tw4jTopology

object App extends Configuration {
  def main(args: Array[String]): Unit = {
    printConfig()
    twitterConfig match {
      case TwitterConfig(_, _, consumerKey, consumerSecret, accessToken, tokenSecret)
        if consumerKey == "" || consumerSecret == "" || accessToken == "" || tokenSecret == "" =>
          AkkaTopology()
      case _ =>
        Tw4jTopology()
    }
  }
}