# kafka-streaming-stockticker

It spins up 5 docker images
1. Zookeeper
2. Broker
3. Schema-Registry
4. Producer
5. Consumer

Producer- creates the topic "stocks" and runs the producer code in scala
Consumer - reads from the topic "stocks" at an interval of 30 secs and prints the average stock price in the console

Instructions

* Clone/Download the repo to the local system
* Open the terminal ,navigate to directory and run docker-compose up -d
* The cluster should spin up in the folllowing sequence Zookeeper -> Broker -> Schema-Registry -> Producer -> Consumer.

If any one of the node goes down, you can run it again.