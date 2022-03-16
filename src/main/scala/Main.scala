import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.AkkaTopology

object Main extends App with Configuration {
  printConfig()
  AkkaTopology.startTopology()
}