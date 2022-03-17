package com.alawresz.twitterment.configuration

import com.typesafe.scalalogging.StrictLogging

import pureconfig.ConfigSource
import pureconfig.generic.auto._

trait Configuration extends StrictLogging {
  lazy val config             = ConfigSource.default.loadOrThrow[Config]
  lazy val kafkaConfig        = config.kafkaConfig

  lazy val line = "-" * 120
  lazy val configWithLogo = (twitterConfig: String) =>
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
    | twitter:  ${twitterConfig}
    |$line
    |""".stripMargin

  def printAkkaConfig(): Unit = {
    val twitterConfig = config.twitterConfig
    logger.info(configWithLogo(twitterConfig.toString()))
  }

  def printStreamConfig(twitterConfig: TwitterConfig): Unit = {
    logger.info(configWithLogo(twitterConfig.toString()))
  }
}