#!/bin/bash

# 请注意
# 本脚本的作用是把本项目编译的结果保存到deploy文件夹中
# 1. 把项目数据库文件拷贝到docker/db/init-sql
# 2. 编译litemall-admin
# 3. 编译litemall-all模块，然后拷贝到docker/litemall

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cd $DIR/../..
LITEMALL_HOME=$PWD
echo "LITEMALL_HOME $LITEMALL_HOME"

cd $LITEMALL_HOME
mvn clean package
cp -f $LITEMALL_HOME/litemall-all/target/simulatoragv-*-exec.jar $LITEMALL_HOME/docker/litemall/simulatoragv.jar