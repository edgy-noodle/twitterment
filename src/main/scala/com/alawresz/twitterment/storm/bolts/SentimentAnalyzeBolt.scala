package com.alawresz.twitterment.storm.bolts

import com.alawresz.twitterment.TweetModel.TweetData

import org.apache.storm.topology.{OutputFieldsDeclarer, IRichBolt}
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.tuple.{Tuple, Values, Fields}

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.CoreNLPProtos.Sentiment
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations

import java.{util => ju}

class SentimentAnalyzeBolt extends IRichBolt {
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
    val tweet     = tuple.getValueByField("tweet").asInstanceOf[TweetData]
    val lang      = tuple.getStringByField("lang")
    lang match {
      case "en" =>
        val sentiment = findSentiment(tweet.text).toString()
        println((tweet, sentiment))
        _collector.emit(tuple, new Values(tweet, sentiment))
        _collector.ack(tuple)
      case _    =>
        // no need to process if lang isn't English
        _collector.ack(tuple)
    }
  }

  override def cleanup(): Unit = {
    // no cleanup needed
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit =
    declarer.declare(new Fields("tweet", "sentiment"))

  override def getComponentConfiguration(): ju.Map[String,Object] =
    _conf  
}

object SentimentAnalyzeBolt {
  def apply(): SentimentAnalyzeBolt =
    new SentimentAnalyzeBolt()
}