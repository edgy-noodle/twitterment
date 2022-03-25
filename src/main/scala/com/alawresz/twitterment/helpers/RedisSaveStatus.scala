package com.alawresz.twitterment.helpers

import com.alawresz.twitterment.configuration.Configuration

import redis.clients.jedis.{JedisPool, JedisPoolConfig}
import scala.util.{Using, Try, Failure, Success}

object RedisSaveStatus extends Configuration {
  def apply(field: String): Unit = {
    Using(new JedisPool(new JedisPoolConfig(), redisConfig.hostAddress)) { pool =>
      Try { pool.getResource() } match {
        case Failure(exception) =>
          logger.warn(exception.getMessage())
        case Success(jedis)     =>
          jedis.hincrBy(redisConfig.keys.statusKey, field, 1)
      }
    }
  }
}