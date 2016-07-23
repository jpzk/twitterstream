# Twitter Streaming API Example with Kafka Streams in Scala

This worker app reads from Twitter's Streaming API and pushes the raw JSON status messages filtered by filter terms to a specified Kafka topic. The aggregation apps read from the Kafka topic, and does aggregation with Kafka Streams on the data.

Not finished yet. 
