#!/bin/bash

PEM_FILE=${PEM_FILE:-/home/zhong_s/script/hillwater-ec2-key.pem}
WORDSEARCH_HOST=${WORDSEARCH_HOST:-www.hillwater.xyz}
UPLOAD_FOLDER=${UPLOAD_FOLDER:-"~/workspace/wordsearch/target"}
VERSION=0.0.1-SNAPSHOT

echo "### Stop Existed Server."
ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "ps aux | grep -i wordsearch-$VERSION | awk '{print $2}' | xargs kill -9"

ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "rm -fr $UPLOAD_FOLDER"
ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "mkdir -p $UPLOAD_FOLDER"
ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "mkdir -p $UPLOAD_FOLDER/logs"

echo "### UPLOAD FILES."
scp -i ${PEM_FILE} ../target/wordsearch-$VERSION.war ubuntu@${WORDSEARCH_HOST}:$UPLOAD_FOLDER/

echo "### Start Servers"
ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "java -DdictionaryPath=/home/ubuntu/workspace/wordsearch/dictionary -Xmx256m -server -jar $UPLOAD_FOLDER/wordsearch-$VERSION.war >$UPLOAD_FOLDER/logs/wordsearch_start.log 2>&1 &" &


sleep 5

echo "### Restart Nginx"
ssh -i ${PEM_FILE} ubuntu@${WORDSEARCH_HOST} "sudo service nginx restart"

echo "COMPLETED!!"
