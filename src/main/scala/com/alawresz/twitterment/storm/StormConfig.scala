package com.alawresz.twitterment.storm

import com.alawresz.twitterment.storm.spouts.InSpoutConfig
import com.alawresz.twitterment.storm.bolts.InBoltConfig

case class StormConfig(inSpout: InSpoutConfig, inBolt: InBoltConfig)