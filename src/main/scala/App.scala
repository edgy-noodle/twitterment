import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.storm.akkatopology._
import com.alawresz.twitterment.TweetStream
import com.alawresz.twitterment.TweetProducer

object App extends Configuration {
  def main(args: Array[String]): Unit = {
    if(args.length == 0) {
      printAkkaConfig()
      AkkaTopology()
    } else {
      val twitterConfig = TwitterConfig(args(0), args(1), args(2), args(3))
      printTw4jConfig(twitterConfig)
      val tweetProducer = TweetProducer()
      val tweetStream = TweetStream(twitterConfig, tweetProducer)
    }
  }
}