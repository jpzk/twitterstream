#!/bin/bash

sbt twitterstream/assembly

cp application.conf docker/ingest/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/ingest/twitterstream.jar

cp application.conf docker/aggregation/application.conf
cp twitterstream/target/scala-2.11/twitterstream.jar docker/aggregation/twitterstream.jar

docker-compose up --build
