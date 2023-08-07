FROM ibmjava:8-sfj

MAINTAINER "Nina Madra"

EXPOSE 8080

WORKDIR /usr/local/bin

COPY ./target/task1-0.0.1-SNAPSHOT.jar webapp.jar

CMD ["java", "-jar", "webapp.jar"]