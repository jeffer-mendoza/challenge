FROM gcr.io/google_appengine/jetty9
VOLUME /tmp
ADD ./target/challenge-0.0.2-SNAPSHOT.jar /app.jar
CMD java -jar /app.jar