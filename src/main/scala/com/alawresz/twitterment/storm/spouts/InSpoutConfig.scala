package com.alawresz.twitterment.storm.spouts

case class InSpoutConfig(
  bootstrapServers: List[String],
  groupId: String,
  topic: String,
  fetchMinBytes: Int
)