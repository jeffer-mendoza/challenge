FROM gcr.io/google_appengine/jetty9
VOLUME /tmp
RUN mkdir /home/config
COPY ./bussiness.properties /home/config/bussiness.properties
ADD ./challenge-1.1.jar /app.jar
CMD java -jar /app.jar