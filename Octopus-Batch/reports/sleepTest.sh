#!/bin/bash

SLEEP=#{DELAY_SEND_EMAIL_REPORTS}

echo "Executing sleep test"
sleep 5
echo "First sleep after 5s"
sleep 10
echo "Second sleep after 10s"
sleep 60
echo "Third sleep after 60s"
sleep 1m
echo "4th sleep after 1m"

echo "------------"
echo "Using octopus sleep variable"

echo ${SLEEP}
echo "Trying to set a sleep with octopus variable..."
sleep ${SLEEP}
echo "Sleep after 60s using octopus variable"

exit 0