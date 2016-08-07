FROM anapsix/alpine-java

ADD application.conf /application.conf
ADD twitterstream.jar /twitterstream.jar

CMD ["java","-cp","twitterstream.jar","mwt.twitterstream.AggregationApp"]

