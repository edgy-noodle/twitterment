# TWITTER SENTIMENT ANALYSIS

![twitter ci](https://github.com/edgy-noodle/twitter/actions/workflows/ci.yaml/badge.svg)

The program can run in 2 configurations:
- (default) without command line arguments

The app will use Akka to access artificial tweets via web requests to Twitter API.

- with Twitter API key/secret and access token/secret provided via command line

The app will use Twitter4J stream and produce to a Kafka topic using.

## How to run
- ### Kafka
```bash
cd docker
docker-compose up
```