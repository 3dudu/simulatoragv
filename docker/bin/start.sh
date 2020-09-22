#!/bin/bash

AppName=simulatoragv.jar
APP_HOME=`pwd`/`dirname $0`

mkdir -p $APP_HOME/logs

echo `date` >> $APP_HOME/${HOSTNAME}.log
echo "`date` Start $AppName ..." >>  $APP_HOME/${HOSTNAME}.log
#JVM参数
JVM_OPTS="-Dname=$AppName -Duser.home=$APP_HOME -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError"
LOG_FILE=$APP_HOME/${HOSTNAME}.log

if [ "$AppName" = "" ];
then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

function start()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`

	if [ x"$PID" != x"" ]; then
	    echo "$AppName is running..."
	else
		nohup java -server -jar  $JVM_OPTS $APP_HOME/$AppName > $LOG_FILE 2>&1 &
		echo "Start $AppName success..."
		echo "`date` Start $AppName success..." >> $APP_HOME/${HOSTNAME}.log
	fi
}

function stop()
{
    echo "Stop $AppName"
	
	PID=""
	query(){
		PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
	}

	query
	if [ x"$PID" != x"" ]; then
		kill -TERM $PID
		echo "$AppName (pid:$PID) exiting..."
		echo "`date` $AppName (pid:$PID) exiting..." >> $APP_HOME/${HOSTNAME}.log
		
		while [ x"$PID" != x"" ]
		do
			sleep 1
			query
		done
		echo "$AppName exited."
		echo "`date` $AppName exited." >> $APP_HOME/${HOSTNAME}.log
	else
		echo "$AppName already stopped."
	fi
}

function restart()
{
    stop
    sleep 2
    start
}

function status()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi
}

case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    *)
	restart
esac




