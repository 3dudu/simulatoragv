#!/bin/bash

# 请注意

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cd $DIR/../..
LITEMALL_HOME=$PWD
echo "LITEMALL_HOME $LITEMALL_HOME"

cd $LITEMALL_HOME
mvn clean package
cp -f $LITEMALL_HOME/target/simulatoragv-exec.jar $LITEMALL_HOME/docker/simulatoragv/simulatoragv.jar