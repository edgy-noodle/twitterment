package com.alawresz.twitterment.web

import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

case class TweetData(id: String, text: String)
case class Tweet(data: TweetData)

trait TweetJsonSupport extends SprayJsonSupport {
  implicit val tweetDataFormat = jsonFormat2(TweetData)
  implicit val tweetFormat = jsonFormat1(Tweet)
}