import com.alawresz.twitterment.configuration.Configuration
import com.alawresz.twitterment.storm.Topology

object Main extends App with Configuration {
  printConfig()
  Topology.startTopology()
}