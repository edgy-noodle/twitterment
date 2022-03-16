package com.alawresz.twitterment

import com.alawresz.twitterment.configuration.TwitterConfig

import twitter4j.conf.{Configuration, ConfigurationBuilder}
import twitter4j.{TwitterStreamFactory, TwitterStream}

object TweetStream {
  private val twitterStreamConfig = (twitterConfig: TwitterConfig) => {
    val builder = new ConfigurationBuilder()
    builder.setOAuthConsumerKey(twitterConfig.consumerKey)
    builder.setOAuthConsumerSecret(twitterConfig.consumerSecret)
    builder.setOAuthAccessToken(twitterConfig.accessToken)
    builder.setOAuthAccessTokenSecret(twitterConfig.tokenSecret)
    builder.build()
  }

  def apply(config: TwitterConfig): TwitterStream =
    new TwitterStreamFactory(twitterStreamConfig(config)).getInstance()
}