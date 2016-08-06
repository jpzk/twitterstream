#!/bin/bash

sbt twitterstream/assembly

cp application.conf docker/ingest/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/ingest/twitterstream.jar

cp application.conf docker/aggregation/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/aggregation/twitterstream.jar

cd docker/ingest; docker build -t twitterstream_ingest .; cd - 
cd docker/aggregation; docker build -t twitterstream_aggregation .; cd -

docker-compose up --force-recreate
