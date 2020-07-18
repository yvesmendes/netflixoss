FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/configserver
RUN cd /tmp/ && \
    curl -L -b "oraclelicense=a" http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip -O && \
    unzip jce_policy-8.zip && \
    rm jce_policy-8.zip && \
    yes |cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/local/configserver/configserver.jar
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
