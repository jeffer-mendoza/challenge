FROM gcr.io/google_appengine/jetty9
VOLUME /tmp
RUN mkdir /home/config
COPY ./bussiness.properties /home/config/bussiness.properties
ADD ./challenge-0.0.2-SNAPSHOT.jar /app.jar
CMD java -jar /app.jar