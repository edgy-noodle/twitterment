package com.alawresz.twitterment.configuration

import com.alawresz.twitterment.storm.StormConfig

case class TwitterConfig(
  consumerKey: String,
  consumerSecret: String,
  accessToken: String,
  tokenSecret: String
)

case class TwitterAkkaConfig(
  bearerToken: String,
  url: String
)

case class Config(stormConfig: StormConfig, twitterConfig: TwitterAkkaConfig)