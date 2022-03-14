import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.web.AkkaRequest

object Main extends App with Configuration {
  printConfig()

  val akkaRequest = AkkaRequest(
    config.twitterConfig.bearerToken,
    config.twitterConfig.url
  )

  val tweets = akkaRequest.getTweets(println)
}