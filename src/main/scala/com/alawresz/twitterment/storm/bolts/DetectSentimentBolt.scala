package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.helpers.TweetModel.TweetData
import com.alawresz.twitterment.storm.TupleModel

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.CoreNLPProtos.Sentiment
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations

import com.typesafe.scalalogging.LazyLogging
import scala.util.{Try, Failure, Success}
import java.{util => ju}

class DetectSentimentBolt extends IRichBolt with LazyLogging {
  var _collector: OutputCollector   = _
  var _conf: ju.Map[String, Object] = _
  var _pipeline: StanfordCoreNLP    = _

  private def findSentiment(text: String): Sentiment = {
    val (_, sentiment) = extractSentiments(text)
      .maxBy {
        case (sentence, _) => sentence.length()
      }
    sentiment
  }

  private def extractSentiments(text: String): List[(String, Sentiment)] = {
    import scala.collection.JavaConverters._

    val annotation  = _pipeline.process(text)
    val sentences   = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    val trees       = sentences.asScala.map { sentence =>
      (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree]))
    }
    trees.map { 
      case (sentence, tree) => 
        (sentence.toString(), Sentiment.forNumber(RNNCoreAnnotations.getPredictedClass(tree)))
    }.toList
  }

  override def prepare(conf: ju.Map[String,Object], context: TopologyContext, 
    collector: OutputCollector): Unit = {
      val nlpProps  = {
        val props   = new ju.Properties()
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
        props
      }

      _collector    = collector
      _conf         = conf 
      _pipeline     = new StanfordCoreNLP(nlpProps)
    }

  override def execute(tuple: Tuple): Unit = {
    Try {
      val tweet = tuple.getValueByField(TupleModel.tweet).asInstanceOf[TweetData]
      val lang  = tuple.getStringByField(TupleModel.lang)
      (tweet, lang)
    } match {
      case Failure(exception)     =>
        logger.warn(exception.toString())
        // no need to process if lang can't be detected
      case Success((tweet, lang)) =>
        lang match {
          case "en" =>
            val sentiment = findSentiment(tweet.text).toString()
            _collector.emit(tuple, new Values(tweet, sentiment))
          case _    =>
            // no need to process if lang isn't English
            
        }
    }
    _collector.ack(tuple)
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields(TupleModel.tweet, TupleModel.sentiment))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf  
}

object DetectSentimentBolt {
  def apply(): DetectSentimentBolt =
    new DetectSentimentBolt()
}