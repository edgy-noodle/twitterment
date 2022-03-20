package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.TweetModel.TweetSerialization

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import java.{util => ju}
import com.optimaize.langdetect.profiles.LanguageProfileReader
import com.optimaize.langdetect.{LanguageDetector, LanguageDetectorBuilder}
import com.optimaize.langdetect.ngram.NgramExtractors
import com.optimaize.langdetect.text.{TextObjectFactory, CommonTextObjectFactories}
import scala.util.Try

class LangDetectBolt extends IRichBolt with TweetSerialization {
  var _collector: OutputCollector     = _
  var _conf: ju.Map[String,Object]    = _
  var _langDetect: LanguageDetector   = _
  var _textObject: TextObjectFactory  = _

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      val languageProfiles = new LanguageProfileReader().readAllBuiltIn()
      
      _collector  = collector
      _conf       = conf
      _langDetect = LanguageDetectorBuilder.create(NgramExtractors.standard())
        .withProfiles(languageProfiles)
        .build()
      _textObject = CommonTextObjectFactories.forDetectingOnLargeText()
    }

  override def execute(tuple: Tuple): Unit = {
    val value = tuple.getBinaryByField("value")
    deserialize(value) match {
      case Some(tweet) =>
        Try {
          for {
            text <- Option(_textObject.forText(tweet.text))
            lang <- Option(_langDetect.getProbabilities(text).get(0).getLocale().toString())
          } yield _collector.emit(tuple, new Values(tweet, lang))
        }
      case None =>
        logger.error(s"Couldn't deserialize ${value.toString()}!")
    }
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("tweet", "lang"))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf
}

object LangDetectBolt {
  def apply(): LangDetectBolt =
    new LangDetectBolt()
}