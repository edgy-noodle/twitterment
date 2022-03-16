package com.alawresz.twitterment.web

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

trait AkkaSystem extends LazyLogging {
  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def stop(): Unit = {
    materializer.shutdown()
  }
}