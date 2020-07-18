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
echo "Waiting for the Redis server to start on port $REDIS_PORT"
echo "********************************************************"
while ! `nc -z redis $REDIS_PORT`; do sleep 3; done
echo "*******  redis Server has started"

echo "********************************************************"
echo "Waiting for the DATABASESERVER to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASESERVER_PORT`; do sleep 3; done
echo "*******  database Server has started"

echo "********************************************************"
echo "Waiting for the AUTHENTICATION to start on port $AUTHENTICATION_PORT"
echo "********************************************************"
while ! `nc -z authenticationservice $AUTHENTICATION_PORT`; do sleep 3; done
echo "*******  authenticationservice Server has started"

echo "********************************************************"
echo "Waiting for the organization server to start on port $ORGSERVER_PORT"
echo "********************************************************"
while ! `nc -z organizationservice $ORGSERVER_PORT`; do sleep 3; done
echo "*******  organization Server has started"

echo "********************************************************"
echo "Waiting for the organization server to start on port $LICENSINGSERVER_PORT"
echo "********************************************************"
while ! `nc -z licensingservice $LICENSINGSERVER_PORT`; do sleep 3; done
echo "*******  LICENSING Server has started"

echo "********************************************************"
echo "Starting Zuul Service with $SERVER_PORT"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /usr/local/zuulservice/zuulservice.jar
