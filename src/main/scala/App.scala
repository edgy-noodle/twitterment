import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.configuration.TwitterConfig
import com.alawresz.twitterment.storm.AkkaTopology
import com.alawresz.twitterment.TweetStream

object App extends Configuration {
  def main(args: Array[String]): Unit = {
    printConfig()

    if(args.length == 0) {
      AkkaTopology.startTopology()
    } else {
      val twitterConfig = TwitterConfig(args(0), args(1), args(2), args(3))
      val tweetStream = TweetStream(twitterConfig)
    }
  }
}