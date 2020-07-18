#!/bin/sh
echo "********************************************************"
echo "Waiting for the Database server to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASESERVER_PORT`; do sleep 3; done
echo "*******  DATABASE Server has started"

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekaserver $EUREKASERVER_PORT`; do sleep 3; done
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
echo "*******  REDIS Server has started"


echo "********************************************************"
echo "Starting Authentication Service                           "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Dspring.profiles.active=$PROFILE                                   \
     -jar /usr/local/authenticationservice/authentication-service.jar
