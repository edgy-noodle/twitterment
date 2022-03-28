package com.alawresz.twitterment.configuration

case class StormConfig(
  topologyName: String,
  debug: Boolean,
  numWorkers: Int,
  javaSerialization: Boolean,
  maxSpoutPending: Int,
  messageTimeout: Int
)

case class KafkaConfig(producer: ProdConfig, consumer: ConsConfig)
case class ProdConfig(
  bootstrapServers: String,
  clientId: String,
  topic: String,
  batchSize: Int,
  lingerMs: Long,
  acks: String
)
case class ConsConfig(
  bootstrapServers: String,
  groupId: String,
  topic: String,
  fetchMinBytes: Int
)

case class TwitterConfig(
  bearerToken: String,
  url: String,
  consumerKey: String = "",
  consumerSecret: String = "",
  accessToken: String = "",
  tokenSecret: String = ""
)

case class RedisConfig(
  hostAddress: String,
  hostPort: Int,
  keys: RedisKeys
)
case class RedisKeys(
  sentimentKey: String, 
  langKey: String,
  statusKey: String
)

case class Config(
  stormConfig: StormConfig,
  kafkaConfig: KafkaConfig, 
  twitterConfig: TwitterConfig, 
  redisConfig: RedisConfig
)