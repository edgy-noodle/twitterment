package com.alawresz.twitterment.web

import com.typesafe.scalalogging.StrictLogging
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait AkkaSystem extends StrictLogging {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def stop(): Unit = {
    materializer.shutdown()
  }
}