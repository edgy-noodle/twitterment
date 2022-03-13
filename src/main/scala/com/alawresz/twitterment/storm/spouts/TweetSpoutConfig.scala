package com.alawresz.twitterment.storm.spouts

case class TweetSpoutConfig(
  bootstrapServers: List[String],
  groupId: String,
  topic: String,
  fetchMinBytes: Int
)