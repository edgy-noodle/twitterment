package com.alawresz.twitterment.web

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader

import scala.util.Failure
import scala.util.Success

case class AkkaRequest(token: String) extends AkkaSystem {
  import system.dispatcher

  // val responseFuture = Http()
  // .singleRequest(HttpRequest(
  //   HttpMethods.GET,
  //   "https://api.twitter.com/2/tweets/20", 
  //   Seq(RawHeader("Authorization", s"Bearer $token"))
  // ))
  // val response = responseFuture.onComplete {
  //   case Failure(exception) => logger.error(exception.toString())
  //   case Success(value) => println(value)
  // }
}