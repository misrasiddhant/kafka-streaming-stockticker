
echo "Environment Variable Path: $PATH"

echo "Starting Spark services"
start-all.sh

echo "Running Spark Code"
spark-shell -i /opt/spark_consumer.scala --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 