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

case class AkkaRequest(token: String, url: String) extends AkkaSystem with TweetJsonSupport {
  import system.dispatcher
  
  private val authHeader = RawHeader("Authorization", s"Bearer $token")
  private val request = HttpRequest(
    method = HttpMethods.GET,
    uri = url,
    headers = List(authHeader)
  )

  def getTweets(f: Tweet => Unit): Unit = {
    val responseFuture = Http()
      .singleRequest(request)

    responseFuture.transformWith {
      case Success(response) if response.status == StatusCodes.OK =>
        response.entity.dataBytes.runForeach { chunk =>
          val tweetFuture = Unmarshal(chunk).to[Tweet]
          processTweet(tweetFuture)(f)
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

  private def processTweet(tweetFuture: Future[Tweet])(f: Tweet => Unit) = {
    tweetFuture.foreach(f)
  }
}