@echo off
title L2Phoenix Nova Edition: Login Server Console
:start
echo Starting L2Phoenix Nova Edition Login Server.
echo.
java -Xms32m -Xmx32m -cp javolution.jar;c3p0-0.9.1.2.jar;mysql-connector-java-bin.jar;l2pserver.jar;jacksum.jar l2p.loginserver.L2LoginServer
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restart ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly
echo.
:end
echo.
echo server terminated
echo.
pause