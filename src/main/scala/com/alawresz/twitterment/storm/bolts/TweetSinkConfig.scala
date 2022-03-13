package com.alawresz.twitterment.storm.bolts

case class TweetSinkConfig(
  bootstrapServers: List[String],
  clientId: String,
  topic: String,
  batchSize: Int,
  lingerMs: Short,
  acks: String
)