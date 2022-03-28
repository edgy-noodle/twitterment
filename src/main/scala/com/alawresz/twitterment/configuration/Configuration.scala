package com.alawresz.twitterment.configuration

import com.typesafe.scalalogging.StrictLogging

import pureconfig.ConfigSource
import pureconfig.generic.auto._

trait Configuration extends StrictLogging {
  lazy val config         = ConfigSource.default.loadOrThrow[Config]
  lazy val kafkaConfig    = config.kafkaConfig
  lazy val twitterConfig  = config.twitterConfig
  lazy val redisConfig    = config.redisConfig
  lazy val stormConfig    = config.stormConfig
  lazy val graphiteConfig = config.graphiteConfig

  lazy val line           = "-" * 120
  val configWithLogo =
    s"""
    |  _________  ___       __   ___  _________  _________  _______   ________  _____ ______   _______   ________   _________   
    | |\\___   ___\\\\  \\     |\\  \\|\\  \\|\\___   ___\\\\___   ___\\\\  ___ \\ |\\   __  \\|\\   _ \\  _   \\|\\  ___ \\ |\\   ___  \\|\\___   ___\\ 
    | \\|___ \\  \\_\\ \\  \\    \\ \\  \\ \\  \\|___ \\  \\_\\|___ \\  \\_\\ \\   __/|\\ \\  \\|\\  \\ \\  \\\\\\__\\ \\  \\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_| 
    |      \\ \\  \\ \\ \\  \\  __\\ \\  \\ \\  \\   \\ \\  \\     \\ \\  \\ \\ \\  \\_|/_\\ \\   _  _\\ \\  \\\\|__| \\  \\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\  
    |       \\ \\  \\ \\ \\  \\|\\__\\_\\  \\ \\  \\   \\ \\  \\     \\ \\  \\ \\ \\  \\_|\\ \\ \\  \\\\  \\\\ \\  \\    \\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\ 
    |        \\ \\__\\ \\ \\____________\\ \\__\\   \\ \\__\\     \\ \\__\\ \\ \\_______\\ \\__\\\\ _\\\\ \\__\\    \\ \\__\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\
    |         \\|__|  \\|____________|\\|__|    \\|__|      \\|__|  \\|_______|\\|__|\\|__|\\|__|     \\|__|\\|_______|\\|__| \\|__|    \\|__|
    |$line
    | TWITTERMENT Configuration:
    |$line
    | in-spout: ${kafkaConfig.consumer}
    | in-bolt:  ${kafkaConfig.producer}
    | storm:    ${stormConfig}
    | twitter:  ${twitterConfig}
    | redis:    ${redisConfig}
    | graphite: ${graphiteConfig}
    |$line
    |""".stripMargin

  def printConfig(): Unit = {
    logger.info(configWithLogo)
  }
}