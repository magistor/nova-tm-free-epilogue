#!/bin/bash

while :;
do
	mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
	mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
	mv log/chat.log "log/`date +%Y-%m-%d_%H:%M:%S`-chat.log"
	nice -n -2 java -server -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m -cp bsf.jar:javolution.jar:bsh-2.0.jar:c3p0-0.9.1.2.jar:mysql-connector-java-bin.jar:jacksum.jar:l2pserver.jar l2p.gameserver.GameServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
	sleep 10;
done