package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.storm.akkatopology.AkkaTopology
import com.alawresz.twitterment.storm.tw4jtopology.Tw4jTopology

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


trait TestBase extends AnyWordSpec with Matchers with Configuration {
  printConfig()
  // twitterConfig match {
  //   case TwitterConfig(_, _, consumerKey, consumerSecret, accessToken, tokenSecret)
  //     if consumerKey == "" || consumerSecret == "" || accessToken == "" || tokenSecret == "" =>
  //       AkkaTopology()
  //   case _ =>
  //     Tw4jTopology()
  // }
}