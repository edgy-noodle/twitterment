package com.alawresz.twitterment

import com.typesafe.scalalogging.LazyLogging

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object TweetModel {
  case class TweetData(id: String, text: String)
  case class Tweet(data: TweetData)

  trait TweetSerialization extends LazyLogging {
    def serialize(tweet: TweetData): Array[Byte] =
      tweet.asJson.noSpaces.getBytes()

    def deserialize(bytes: Array[Byte]): Option[TweetData] = {
      val string = new String(bytes)
      val bytesOrError = decode[TweetData](string)
      bytesOrError match {
        case Right(tweet) =>
          Some(tweet)
        case Left(error) =>
          logger.error(error.toString())
          Option.empty
      }
    }
  }
}
