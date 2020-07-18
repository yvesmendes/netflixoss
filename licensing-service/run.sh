#!/bin/sh
echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekaserver  $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"


echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z configserver $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the Database server to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASESERVER_PORT`; do sleep 3; done
echo "*******  Database Server has started"

echo "********************************************************"
echo "Waiting for the Redis server to start on port $REDIS_PORT"
echo "********************************************************"
while ! `nc -z redis $REDIS_PORT`; do sleep 3; done
echo "*******  Redis Server has started"

echo "********************************************************"
echo "Waiting for the KAFKA server to start on port $KAFKA_PORT"
echo "********************************************************"
while ! `nc -z kafkaserver $KAFKA_PORT`; do sleep 3; done
echo "*******  KAFKA Server has started"

echo "********************************************************"
echo "Starting License Server with Configuration Service via Eureka" ON PORT: $SERVER_PORT;
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.profiles.active=$PROFILE -jar /usr/local/licensingservice/licensingservice.jar
