package com.alawresz.twitterment.storm.bolts

case class InBoltConfig(
  bootstrapServers: List[String],
  clientId: String,
  topic: String,
  batchSize: Int,
  lingerMs: Long,
  acks: String
)