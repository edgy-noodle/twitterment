package com.alawresz.twitterment.storm

import org.apache.storm.tuple.{Fields, Values}

object TupleModel {
  val key = "key"
  val value = "value"

  val default = new Fields(key, value)
}