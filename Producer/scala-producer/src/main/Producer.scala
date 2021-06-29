import java.util.Properties
import java.util.logging.Logger

import Datatypes._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

object Producer {
  var logger = Logger.getLogger(this.getClass.getName)

  var bootstrap_servers = "localhost:9092"
  var schema_registry_url = "http://localhost:8081"
  var topicName = "stocks"

  def main(args: Array[String]): Unit = {

    if(args.length <3 ){
      logger.info("Invalid number of arguments")
      //System.exit(1)
    }else{
      bootstrap_servers = args(0)
      schema_registry_url = args(1)
      topicName = args(2)
    }
    val basePrice = scala.collection.mutable.Map("AMZN" -> 1902, "MSFT" -> 107, "AAPL" -> 215)

    while (true) {
		
	/* Generating new stock ticker price within +/- 10% */
      val ticker = basePrice.keys.map {
        x =>
          new Ticker(x, basePrice(x) * (1 + (scala.util.Random.nextInt(21) - 10).floatValue / 100))
      }

      
      var t= new Tickers(Array(ticker.toList:_*))
      sendToKafka(t)
      Thread.sleep(100)

    }
  }

  def sendToKafka(jsonData: Int): Unit = {
    val config = new Properties()
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers)
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer")
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    var tickerProducer = new KafkaProducer[Long, String](config)


    val record = new ProducerRecord[Long, String](topicName, System.currentTimeMillis(), "jsonData")
    var future = tickerProducer.send(record)

    tickerProducer.flush()
    tickerProducer.close()

  }

  def sendToKafka(jsonData: Tickers): Unit = {
    val config = new Properties()
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers)
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer")
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer")
    config.put("schema.registry.url", schema_registry_url)

    var tickerProducer = new KafkaProducer[Long, Tickers](config)


    val record = new ProducerRecord[Long, Tickers](topicName, System.currentTimeMillis(), jsonData)
    tickerProducer.send(record)

    tickerProducer.flush()
    tickerProducer.close()
  }
}
