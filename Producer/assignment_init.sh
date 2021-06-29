
export PATH=$PATH:/opt/scala/bin:/opt/mvn/bin
echo $PATH

TOPIC_NAME="stocks"

echo "Checking if topic exists"
topic=`sh /opt/kafka/bin/kafka-topics.sh -list -bootstrap-server broker:29092 --topic $TOPIC_NAME`
if [ "$topic" != "$TOPIC_NAME" ]
then
	echo "Creating topic : $TOPIC_NAME"
	sh /opt/kafka/bin/kafka-topics.sh -create -bootstrap-server broker:29092 --topic $TOPIC_NAME
else
	echo "Topic Exists"
fi 

echo "running Scala producer"
# executes the scala code, 
# @Param1 : Broker 
# @Param2 : Schema registry URL
# @Param3 : Topic Name
scala -classpath /opt/scala-producer/target/scala-producer-1.0-SNAPSHOT-jar-with-dependencies.jar Producer broker:29092 http://schema-registry:8081 $TOPIC_NAME
