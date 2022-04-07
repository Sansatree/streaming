FROM openjdk:11-jdk
VOLUME /tmp
ADD build/libs/streamingserver-0.0.1-SNAPSHOT.jar streamingserver.jar
ENTRYPOINT ["java", "-jar", "streamingserver.jar"]
