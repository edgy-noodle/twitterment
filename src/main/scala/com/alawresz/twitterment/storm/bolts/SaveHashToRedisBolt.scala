package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.configuration.Configuration

import org.apache.storm.redis.common.config.JedisPoolConfig
import org.apache.storm.redis.bolt.AbstractRedisBolt
import org.apache.storm.tuple.Tuple
import org.apache.storm.topology.OutputFieldsDeclarer

import com.typesafe.scalalogging.LazyLogging
import scala.util.{Try, Failure, Success}

class SaveHashToRedisBolt(key: String, field: String, poolConfig: JedisPoolConfig) 
  extends AbstractRedisBolt(poolConfig) with LazyLogging {
    override protected def process(tuple: Tuple): Unit = {
      // nothing to do
    }

    override def execute(tuple: Tuple): Unit = {
      Try { tuple.getStringByField(field) } match {
        case Failure(exception)   =>
          logger.warn(exception.getMessage())
          collector.ack(tuple)
        case Success(fieldValue)  =>
          Try {
            val jedisCommands = getInstance()
            jedisCommands.hincrBy(key, fieldValue, 1)
            returnInstance(jedisCommands)
          } match {
            case Failure(exception) =>
              logger.warn(exception.getMessage())
              // no acking, will retry
            case Success(_) =>
              collector.ack(tuple)
          }
      }
    }

    override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
      // no output
    }
}

object SaveHashToRedisBolt extends Configuration {
  private val poolConfig = 
    new JedisPoolConfig.Builder()
      .setHost(redisConfig.hostAddress)
      .setPort(redisConfig.hostPort)
      .build()
  
  def apply(key: String, field: String) =
    new SaveHashToRedisBolt(key, field, poolConfig)
}