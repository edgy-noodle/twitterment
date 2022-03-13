package com.alawresz.twitterment.web

import scala.concurrent.duration._
import scala.concurrent.Future

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import scala.util.Failure
import scala.util.Success
import scala.concurrent.Await
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

case class TweetData(id: String, text: String)
case class Tweet(data: TweetData)

case class AkkaRequest(token: String, url: String) extends AkkaSystem with SprayJsonSupport {
  import system.dispatcher
  private val authHeader = RawHeader("Authorization", s"Bearer $token")
  implicit val tweetDataFormat = jsonFormat2(TweetData)
  implicit val tweetFormat = jsonFormat1(Tweet)

  def setRules(rules: String): Unit = {
    
  }

  def deleteRules(rules: String): Unit = {
    
  }

  def getTweetFuture() = {
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = url,
      headers = List(authHeader)
    )
    val response = Http()
      .singleRequest(request)
    response.transformWith {
      case Success(response) if response.status == StatusCodes.OK =>
        response.entity.dataBytes.scan("") { (acc, curr) =>
          if (acc.contains("\r\n")) curr.utf8String
          else acc + curr.utf8String
        }.filter(_.contains("\r\n"))
        .runForeach { json =>
          logger.info(json)
        }
        Unmarshal(response.entity).to[Tweet]
      case Success(response) =>
        response.discardEntityBytes()
        logger.error(response.toString())
        Future.failed(new Exception(response.toString()))
      case Failure(exception) =>
        logger.error(exception.toString())
        Future.failed(exception)
    }
  }

  def getTweet() = {
    val tweetFuture = getTweetFuture()
    tweetFuture.foreach(println)
  }
}