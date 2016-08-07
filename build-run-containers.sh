#!/bin/bash

sbt twitterstream/assembly

cp application.conf docker/ingest/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/ingest/twitterstream.jar

cp application.conf docker/aggregation/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/aggregation/twitterstream.jar

docker rm twitterstream_ingest
docker rm twitterstream_aggregation
docker rm twitterstream_kafka
docker rm twitterstream_zookeeper

docker rmi -f twitterstream_ingest
docker rmi -f twitterstream_aggregation

cd docker/ingest; docker build --no-cache -t twitterstream_ingest .; cd - 
cd docker/aggregation; docker build --no-cache -t twitterstream_aggregation .; cd -

docker-compose up --force-recreate
