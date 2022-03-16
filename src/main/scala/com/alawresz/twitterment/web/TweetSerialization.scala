package com.alawresz.twitterment.web

import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Encoder, Decoder}
import com.typesafe.scalalogging.LazyLogging

case class TweetData(id: String, text: String)
case class Tweet(data: TweetData)

trait TweetSerialization extends SprayJsonSupport with LazyLogging {
  implicit val tweetDataFormat  = jsonFormat2(TweetData)
  implicit val tweetFormat      = jsonFormat1(Tweet)

  implicit def serialize(tweet: Tweet): Array[Byte] =
    tweet.asJson.noSpaces.getBytes()
  implicit def deserialize(bytes: Array[Byte]): Option[Tweet] = {
    val string = new String(bytes)
    val bytesOrError = decode[Tweet](string)
    bytesOrError match {
      case Right(tweet) =>
        Some(tweet)
      case Left(error) =>
        logger.error(error.toString())
        None
    }
  }
}