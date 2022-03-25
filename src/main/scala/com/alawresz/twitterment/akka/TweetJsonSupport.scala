package com.alawresz.twitterment.akka

import com.alawresz.twitterment.helpers.TweetModel._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait TweetJsonSupport extends SprayJsonSupport {
  implicit val tweetDataFormat  = jsonFormat2(TweetData)
  implicit val tweetFormat      = jsonFormat1(Tweet)
}