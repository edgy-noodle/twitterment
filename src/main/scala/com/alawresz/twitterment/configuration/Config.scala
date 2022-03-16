package com.alawresz.twitterment.configuration

import com.alawresz.twitterment.storm.StormConfig

case class TwitterConfig(
  consumerKey: String,
  consumerSecret: String,
  bearerToken: String,
  url: String
)

case class Config(stormConfig: StormConfig, twitterConfig: TwitterConfig)