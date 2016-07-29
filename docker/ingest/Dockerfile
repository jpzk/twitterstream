FROM java:8-jre

ADD application.conf /application.conf
ADD ingest.jar /ingest.jar

CMD ["java","-cp","ingest.jar","mwt.twitterstreams.IngestApp"]

