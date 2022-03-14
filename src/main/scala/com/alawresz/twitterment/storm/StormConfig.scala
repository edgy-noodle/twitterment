package com.alawresz.twitterment.storm

import com.alawresz.twitterment.storm.spouts.TweetSpoutConfig
import com.alawresz.twitterment.storm.bolts.TweetSinkConfig

case class StormConfig(tweetsSpout: TweetSpoutConfig, tweetsIn: TweetSinkConfig)