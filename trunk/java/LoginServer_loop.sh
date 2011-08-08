#!/bin/bash

while :;
do
	mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
	mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
	java -server -Xms32m -Xmx32m -cp javolution.jar:c3p0-0.9.1.2.jar:mysql-connector-java-bin.jar:jacksum.jar:l2pserver.jar l2p.loginserver.L2LoginServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
	sleep 10;
done