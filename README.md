# TWITTERMENT

![twitter ci](https://github.com/edgy-noodle/twitter/actions/workflows/ci.yaml/badge.svg)

### TWITTER SENTIMENT ANALYSIS APP
A simple Scala app for analyzing tweet sentiments. The tweets will be analyzed globally with a sample provided via Twitter API. Analysis using additional aggregation rules will be implemented in the future. Integration with Hadoop and Grafana will also be added.

Twitterment is using the following technologies:
- Kafka
- Storm
- Akka
- Pureconfig
- Circe
- Twitter4J
- Language Detector by Optimaize

---

## The app can run in 2 configurations:
- __[ DEFAULT ]__ without arguments

The app will use __Akka__ to access sample tweets via web requests to __Twitter API v2__ and produce to a __Kafka__ topic `tweets.in` using a *BaseRichSpout* and a *KafkaBolt* available via __Storm__.

- with Twitter API key/secret and access token/secret

The app will use a __Twitter4J__ stream and produce to a __Kafka__ topic `tweets.in` using a *KafkaProducer*.

## How to launch
### 1. Start Kafka & Zookeeper
```bash
cd docker
docker-compose up
```
### 2. Run via SBT
- __[ DEFAULT ]__
```bash
sbt run
```
- using Twitter dev account
```bash
sbt 'run $API_KEY $API_SECRET $ACCESS_TOKEN $ACCESS_TOKEN_SECRET'
```