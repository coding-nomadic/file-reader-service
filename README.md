## Download and Extract Kafka
Download the latest Kafka binary from the Apache Kafka Downloads page.

- https://downloads.apache.org/kafka/3.7.0/kafka_2.13-3.7.0.tgz

- tar -xzf kafka_2.13-3.7.0.tgz
- mv kafka_2.13-3.7.0 ~/kafka
- cd ~/kafka

## Start ZooKeeper

Kafka needs ZooKeeper to run (unless you're using KRaft mode in newer versions):

- bin/zookeeper-server-start.sh config/zookeeper.properties

## Start Kafka Server
- cd ~/kafka
- bin/kafka-server-start.sh config/server.properties

## Create Kafka Topic

bin/kafka-topics.sh \
  --create \
  --topic bitcoin-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1

This creates a topic bitcoin-topic with 3 partitions.

## Publish Messages to Topic (Console Producer)

bin/kafka-console-producer.sh \
  --topic bitcoin-topic \
  --bootstrap-server localhost:9092
Type a few messages and hit Enter to send them.

## Consume Messages from Topic (Console Consumer)
bin/kafka-console-consumer.sh \
  --topic bitcoin-topic \
  --bootstrap-server localhost:9092 \
  --from-beginning
