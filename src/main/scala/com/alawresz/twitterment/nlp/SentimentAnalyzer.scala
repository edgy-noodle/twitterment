package com.alawresz.twitterment.nlp

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.CoreNLPProtos.Sentiment
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations

import java.util.Properties

class SentimentAnalyzer(pipeline: StanfordCoreNLP) {
  def findSentiment(text: String): Sentiment = {
    val (_, sentiment) = extractSentiments(text)
      .maxBy {
        case (sentence, _) => sentence.length()
      }
    sentiment
  }

  def extractSentiments(text: String): List[(String, Sentiment)] = {
    import scala.collection.JavaConverters._

    val annotation  = pipeline.process(text)
    val sentences   = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    val trees       = sentences.asScala.map { sentence =>
      (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree]))
    }
    trees.map { 
      case (sentence, tree) => 
        (sentence.toString(), Sentiment.forNumber(RNNCoreAnnotations.getPredictedClass(tree)))
    }.toList
  }
}

object SentimentAnalyzer {
  private val nlpProps = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
    props
  }

  def apply(): SentimentAnalyzer = {
    val pipeline = new StanfordCoreNLP(nlpProps)
    new SentimentAnalyzer(pipeline)
  } 
}
