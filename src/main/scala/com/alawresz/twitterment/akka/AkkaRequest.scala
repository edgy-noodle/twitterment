package com.alawresz.twitterment.akka

import com.alawresz.twitterment.helpers.TweetModel.Tweet

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal

case class AkkaRequest(token: String, url: String) extends TweetJsonSupport with AkkaSystem {
  import system.dispatcher
  
  private val authHeader = RawHeader("Authorization", s"Bearer $token")
  private val request = HttpRequest(
    method  = HttpMethods.GET,
    uri     = url,
    headers = List(authHeader)
  )

  def processTweetWith(f: Future[Tweet] => Unit): Unit = {
    val responseFuture = Http()
      .singleRequest(request)

    responseFuture.andThen {
      case Success(response) if response.status == StatusCodes.OK =>
        val tweetFuture = Unmarshal(response.entity).to[Tweet]
        f(tweetFuture)
      case Success(response) =>
        response.discardEntityBytes()
        logger.error(response.toString())
        Future.failed(new Exception(response.toString()))
      case Failure(exception) =>
        logger.error(exception.getMessage())
        Future.failed(exception)
    }.andThen {
      case _ => system.terminate()
    }
  }
}