#!/bin/bash

sbt ingest/assembly

cp application.conf docker/ingest/
cp ingest/target/scala-2.11/ingest.jar docker/ingest/

docker-compose up --build
