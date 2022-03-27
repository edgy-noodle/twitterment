package com.alawresz.twitterment.configuration

case class RedisKeys(
  sentimentKey: String, 
  langKey: String,
  statusKey: String
)

case class RedisConfig(
  hostAddress: String,
  hostPort: Int,
  keys: RedisKeys
)

case class TwitterConfig(
  bearerToken: String,
  url: String,
  consumerKey: String = "",
  consumerSecret: String = "",
  accessToken: String = "",
  tokenSecret: String = ""
)

case class ConsConfig(
  bootstrapServers: String,
  groupId: String,
  topic: String,
  fetchMinBytes: Int
)

case class ProdConfig(
  bootstrapServers: String,
  clientId: String,
  topic: String,
  batchSize: Int,
  lingerMs: Long,
  acks: String
)

case class KafkaConfig(producer: ProdConfig, consumer: ConsConfig)

case class StormConfig(topologyName: String)

case class Config(
  stormConfig: StormConfig,
  kafkaConfig: KafkaConfig, 
  twitterConfig: TwitterConfig, 
  redisConfig: RedisConfig
)