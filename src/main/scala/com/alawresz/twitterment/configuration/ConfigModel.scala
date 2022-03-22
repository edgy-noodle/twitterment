package com.alawresz.twitterment.configuration

case class TwitterConfig(
  bearerToken: String,
  url: String,
  consumerKey: String = "",
  consumerSecret: String = "",
  accessToken: String = "",
  tokenSecret: String = ""
)

case class ConsConfig(
  bootstrapServers: List[String],
  groupId: String,
  topic: String,
  fetchMinBytes: Int
)

case class ProdConfig(
  bootstrapServers: List[String],
  clientId: String,
  topic: String,
  batchSize: Int,
  lingerMs: Long,
  acks: String
)

case class KafkaConfig(producer: ProdConfig, consumer: ConsConfig)

case class Config(kafkaConfig: KafkaConfig, twitterConfig: TwitterConfig)