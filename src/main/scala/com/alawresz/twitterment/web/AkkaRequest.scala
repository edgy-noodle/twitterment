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
  implicit val tweetDataFormat = jsonFormat2(TweetData)
  implicit val tweetFormat = jsonFormat1(Tweet)
  private val authHeader = RawHeader("Authorization", s"Bearer $token")
  private val request = HttpRequest(
    method = HttpMethods.GET,
    uri = url,
    headers = List(authHeader)
  )

  def getTweet() = {
    val responseFuture = Http()
      .singleRequest(request)

    val tweetFuture = responseFuture.transformWith {
      case Success(response) if response.status == StatusCodes.OK =>
        response.entity.dataBytes.runForeach { chunk =>
          logger.info(Unmarshal(chunk).to[Tweet].map { tweet =>
            tweet.data.toString()
          }.toString())
        }
      case Success(response) =>
        response.discardEntityBytes()
        logger.error(response.toString())
        Future.failed(new Exception(response.toString()))
      case Failure(exception) =>
        logger.error(exception.toString())
        Future.failed(exception)
    }
  }
}