// Defining Class for generating schema
case class Ticker (var name:String, var price :Float)
case class Tickers(var tickers: Array[Ticker])

import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.functions._


// UDF function to remove magic bytes
object deserializeUDF extends scala.Serializable{
	def byteToString(input :Array[Byte]): String={
		return new String(input.slice(5,input.length))
	}
}
var byte_to_json_string = udf((x: Array[Byte]) => deserializeUDF.byteToString(x))


// Generating Schema of Structtype from Tickers calss definition
val schema = ScalaReflection.schemaFor[Tickers].dataType.asInstanceOf[StructType]

//Creating dataframe reading data from Kafka Topic, fetching the JSON string and exploding the records
var df= spark.readStream.format("kafka").option("kafka.bootstrap.servers", "broker:29092").option("value.deserializer","io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer").option("subscribe", "stocks").option("auto.offset.reset","latest").load().select(byte_to_json_string(col("value")).as("value")).withColumn("json",from_json(col("value"),schema)).selectExpr("explode(json.tickers) as ticker").select("ticker.*")

var q = df.groupBy("name").agg(avg(col("price"))).withColumn("time",current_timestamp()).writeStream.format("console").option("truncate",false).trigger(Trigger.ProcessingTime("30 seconds")).outputMode("complete").start()
q.awaitTermination()

