@echo off
title L2Phoenix Nova Edition: Game Server Console
:start
echo Starting L2Phoenix Nova Edition Game Server.
echo.
rem ======== Optimize memory settings =======
rem Minimal size with geodata is 1.5G, w/o geo 1G
rem Make sure -Xmn value is always 1/4 the size of -Xms and -Xmx.
rem -Xms and -Xmx should always be equal.
rem ==========================================
java -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m -cp bsf.jar;bsh-2.0.jar;javolution.jar;c3p0-0.9.1.2.jar;mysql-connector-java-bin.jar;l2pserver.jar;jython.jar;jacksum.jar l2p.gameserver.GameServer
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