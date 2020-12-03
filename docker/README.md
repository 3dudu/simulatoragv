## docker

### 项目打包

1. 在服务器或者开发机打包项目到docker；
    ```   
    cd ..
    mvn clean package
    cp -f ./target/simulatoragv-exec.jar ./docker/simulatoragv/simulatoragv.jar
    ```
    这里的工作是：
    1. 编译simulatoragv项目

       
2. 修改simulatoragv文件夹下面的*.yml外部配置文件，当simulatoragv模块启动时会
    加载外部配置文件，而覆盖默认jar包内部的配置文件。
    例如，配置文件中一些地方需要设置成远程服务器的IP地址
    
此时docker部署包结构如下：

* bin

存放远程服务器运行的脚本，包括deploy.sh脚本

由于是本地开发服务器运行，因此开发者可以不用上传到远程服务器。

* docker-compose.yml

docker-compose配置脚本，运行docker-compose命令会

### 项目部署

1. 云服务器环境安装docker和docker-compose（MySQL和JDK1.8无需安装，因为使用docker自动安装）。
   此外请确保云服务器的安全组已经允许相应的端口。

2. 运行docker-compose
    ```bash
    cd /home/ubuntu/docker
   sudo docker-compose
    ```
