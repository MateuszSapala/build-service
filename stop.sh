#!/bin/bash
for port in {8180..8189}
do
  echo "Stopping service on port: $port"
  taskkill //PID $(netstat -ano | findstr :$port | head -n 1 |grep -o '[0-9]\+$') //F
done
