#!/bin/bash
export AWS_ACCESS_KEY_ID=foo
export AWS_SECRET_ACCESS_KEY=foo

aws --region=eu-west-1 --endpoint-url=http://localhost:4576 sqs create-queue --queue-name order-returned-queue --attributes VisibilityTimeout=1

aws --region=eu-west-1 --endpoint-url=http://localhost:4575 sns create-topic --name order-returned-topic

aws --region=eu-west-1 --endpoint-url=http://localhost:4575 sns subscribe --topic-arn arn:aws:sns:eu-west-1:000000000000:order-returned-topic --protocol sqs --notification-endpoint arn:aws:sqs:eu-west-1:000000000000:order-returned-queue


