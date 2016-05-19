FROM java:8-jdk

MAINTAINER team@breizhcamp.org

EXPOSE 8080
EXPOSE 8443

ADD target/call-for-paper.jar /app.jar
RUN sh -c 'touch /app.jar'

ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar" ]
