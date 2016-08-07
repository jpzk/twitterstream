FROM anapsix/alpine-java:8 

ADD application.conf /application.conf
ADD twitterstream.jar /twitterstream.jar

CMD ["java","-cp","twitterstream.jar","mwt.twitterstream.IngestApp"]

