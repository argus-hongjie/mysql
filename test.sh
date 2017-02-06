#!/bin/bash
while ! mysqladmin ping -h127.0.0.1 -P3351 --silent; do
  echo "$(date) - still trying 3351"
  sleep 1
done
echo "$(date) - connected successfully 3351"
