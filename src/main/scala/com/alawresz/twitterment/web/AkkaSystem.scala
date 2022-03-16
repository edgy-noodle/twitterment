package com.alawresz.twitterment.web

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait AkkaSystem {
  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def stop(): Unit = {
    materializer.shutdown()
  }
}