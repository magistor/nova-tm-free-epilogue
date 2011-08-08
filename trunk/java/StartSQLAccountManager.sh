#!/bin/sh

java -Djava.util.logging.config.file=config/console.cfg -cp c3p0-0.9.1.2.jar:l2pserver.jar:mysql-connector-java-bin.jar l2p.accountmanager.SQLAccountManager