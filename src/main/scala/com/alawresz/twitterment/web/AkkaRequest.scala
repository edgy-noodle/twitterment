package com.alawresz.twitterment.web

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.Await

case class AkkaRequest(token: String, url: String) extends AkkaSystem with TweetJsonSupport {
  import system.dispatcher
  
  private val authHeader = RawHeader("Authorization", s"Bearer $token")
  private val request = HttpRequest(
    method = HttpMethods.GET,
    uri = url,
    headers = List(authHeader)
  )

  def getTweet(id: Int): Tweet = {
    val responseFuture = Http()
      .singleRequest(request)

    val tweetFuture = responseFuture.transformWith {
      case Success(response) if response.status == StatusCodes.OK =>
        val tweet = Unmarshal(response.entity).to[Tweet]
        tweet
      case Success(response) =>
        response.discardEntityBytes()
        logger.error(response.toString())
        Future.failed(new Exception(response.toString()))
      case Failure(exception) =>
        logger.error(exception.toString())
        Future.failed(exception)
    }

    Await.result(tweetFuture, 5.seconds)
  }
  // def getTweets(): Tweet = {
  //   var tweet: Tweet = Tweet(TweetData("", ""))

  //   val responseFuture = Http()
  //     .singleRequest(request)

  //   responseFuture.transformWith {
  //     case Success(response) if response.status == StatusCodes.OK =>
  //       response.entity.dataBytes.runForeach { chunk =>
  //         val tweetFuture = Unmarshal(chunk).to[Tweet]
  //         tweet = tweetFuture.value.get.get
  //       }
  //     case Success(response) =>
  //       response.discardEntityBytes()
  //       logger.error(response.toString())
  //       Future.failed(new Exception(response.toString()))
  //     case Failure(exception) =>
  //       logger.error(exception.toString())
  //       Future.failed(exception)
  //   }
  //   tweet
  // }
}