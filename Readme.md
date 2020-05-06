

SNSTest

* publishes to a topic
* docker-compose.xml runs localstack container and 
    * creates a queue, a topic and subscribes the queue to the topic
* once a message is published to the topic, it should be in the queue:
```shell script
aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url=http://localhost:4576/queue/order-returned-queue
```
* you can delete it with the `receive handle`, or via purge-queue:
```shell script
aws --endpoint-url=http://localhost:4576 sqs purge-queue --queue-url=http://localhost:4576/queue/order-returned-queue
```



Note: 
https://doc.akka.io/docs/alpakka/current/sns.html
uses no endpoint override. endpoint is implicit in topic region.
