# TWITTERMENT

![twitter ci](https://github.com/edgy-noodle/twitter/actions/workflows/ci.yaml/badge.svg)

---

### TWITTER SENTIMENT ANALYSIS APP
A simple Scala app for analyzing tweets. The sample provided via Twitter API is first analyzed to detect the language. Afterwards the tweets in English will undergo further analysis to establish their sentiment. The data is then saved in a Redis data store and can be displayed using Grafana. Metrics are exposed to a Graphite time-series database and are also integrated into Grafana. Analyses with additional filtering rules will be implemented in the future.

Twitterment is using the following technologies:
- Kafka
- Storm
- Akka
- Pureconfig
- Circe
- Twitter4J
- Language Detector by Optimaize
- Stanford CoreNLP
- Redis
- Grafana
- Graphite

![Grafana dashboard](./.github/resources/Grafana.png)

---

## The app can run in 2 configurations:
- __[ DEFAULT ]__

The app will use __Akka__ to access sample tweets via web requests to __Twitter API v2__ and produce to a __Kafka__ topic `tweets.in` using a *BaseRichSpout* and a *KafkaBolt* available via __Storm__.

- with Twitter API key/secret and access token/secret in environment variables

The app will use a __Twitter4J__ stream and produce to a __Kafka__ topic `tweets.in` using a *KafkaProducer*. The variables needed can be seen in `src\main\resources\application.conf` file.
> Note: Twitter dev account needs elevated access in order to use the __Twitter4J__ stream.

---