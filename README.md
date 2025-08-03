bin/zookeeper-server-start.sh config/zookeeper.properties

cd ~/kafka
bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092

bin/kafka-console-consumer.sh --topic test-topic --bootstrap-server localhost:9092 --from-beginning
