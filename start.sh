#!/bin/bash
mvn clean package
mkdir
for port in {8180..8189}
do
  echo "Starting service on port: $port"
  mkdir target/$port
  nohup java -Dserver.port=$port -Ds2s.service.url=https://localhost:$port -Dbuild-service.build-directory=target/$port/builds -jar target/build-service-0.0.3.jar > target/$port/output.log 2>&1 &
done
