package com.alawresz.twitterment.configuration

import com.alawresz.twitterment.storm.StormConfig

case class TwitterAkkaConfig(
  bearerToken: String,
  url: String
)

case class Config(stormConfig: StormConfig, twitterConfig: TwitterAkkaConfig)