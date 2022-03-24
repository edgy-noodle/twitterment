package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.TweetModel.TweetData
import com.alawresz.twitterment.storm.TupleModel

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import com.optimaize.langdetect.profiles.LanguageProfileReader
import com.optimaize.langdetect.{LanguageDetector, LanguageDetectorBuilder}
import com.optimaize.langdetect.ngram.NgramExtractors
import com.optimaize.langdetect.text.{TextObjectFactory, CommonTextObjectFactories}

import com.typesafe.scalalogging.LazyLogging
import scala.util.{Try, Failure, Success}
import java.{util => ju}

class LangDetectBolt extends IRichBolt with LazyLogging {
  var _collector: OutputCollector     = _
  var _conf: ju.Map[String, Object]   = _
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
    val tweet         = tuple.getValueByField(TupleModel.tweet).asInstanceOf[TweetData]
    Try {
      val text        = _textObject.forText(tweet.text)
      val lang        = _langDetect.getProbabilities(text).get(0)
      val probability = lang.getProbability()

      if (probability >= 95.0) lang.getLocale()
      else Failure(new Exception("Could't determine the language with more than 95% confidence."))
    } match {
      case Failure(exception) =>
        logger.error(exception.getMessage())
        // no acking, will retry
      case Success(lang) =>
        _collector.emit(tuple, new Values(tweet, lang))
        _collector.ack(tuple)
    }
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields(TupleModel.tweet, TupleModel.lang))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf
}

object LangDetectBolt {
  def apply(): LangDetectBolt =
    new LangDetectBolt()
}