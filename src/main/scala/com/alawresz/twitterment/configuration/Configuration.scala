package com.alawresz.twitterment.configuration

import com.typesafe.scalalogging.StrictLogging

import pureconfig.ConfigSource
import pureconfig.generic.auto._

case class TwitterConfig(
  consumerKey: String,
  consumerSecret: String,
  accessToken: String,
  tokenSecret: String
)

trait Configuration extends StrictLogging {
  lazy val config             = ConfigSource.default.loadOrThrow[Config]
  lazy val stormConfig        = config.stormConfig
  lazy val twitterConfig      = config.twitterConfig

  def printConfig(): Unit = {
    logger.info(s"""
      |  _________  ___       __   ___  _________  _________  _______   ________  _____ ______   _______   ________   _________   
      | |\\___   ___\\\\  \\     |\\  \\|\\  \\|\\___   ___\\\\___   ___\\\\  ___ \\ |\\   __  \\|\\   _ \\  _   \\|\\  ___ \\ |\\   ___  \\|\\___   ___\\ 
      | \\|___ \\  \\_\\ \\  \\    \\ \\  \\ \\  \\|___ \\  \\_\\|___ \\  \\_\\ \\   __/|\\ \\  \\|\\  \\ \\  \\\\\\__\\ \\  \\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_| 
      |      \\ \\  \\ \\ \\  \\  __\\ \\  \\ \\  \\   \\ \\  \\     \\ \\  \\ \\ \\  \\_|/_\\ \\   _  _\\ \\  \\\\|__| \\  \\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\  
      |       \\ \\  \\ \\ \\  \\|\\__\\_\\  \\ \\  \\   \\ \\  \\     \\ \\  \\ \\ \\  \\_|\\ \\ \\  \\\\  \\\\ \\  \\    \\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\ 
      |        \\ \\__\\ \\ \\____________\\ \\__\\   \\ \\__\\     \\ \\__\\ \\ \\_______\\ \\__\\\\ _\\\\ \\__\\    \\ \\__\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\
      |         \\|__|  \\|____________|\\|__|    \\|__|      \\|__|  \\|_______|\\|__|\\|__|\\|__|     \\|__|\\|_______|\\|__| \\|__|    \\|__|
      |----------------------------------------------------------------------------------------------------------------------------                                                                                                                                                                                                                                                 
      | TWITTERMENT Configuration:
      |----------------------------------------------------------------------------------------------------------------------------
      | in-spout: ${stormConfig.inSpout}
      | in-bolt: ${stormConfig.inBolt}
      | twitter: ${twitterConfig}
      |----------------------------------------------------------------------------------------------------------------------------
      |""".stripMargin)
  }
}