#!/bin/bash

LOG_PATH=/home/ubuntu/log/calc


# 스크립트 로그 파일이 없다면 생성
mkdir -p $LOG_PATH


# MySQL 컨테이너 이름
MYSQL_CONTAINER_NAME=mysql

# MySQL 컨테이너가 실행 중인지 확인하고 실행되지 않은 경우 시작
if [ ! "$(sudo docker ps -q -f name=$MYSQL_CONTAINER_NAME)" ]; then
    echo "Starting MySQL container..."
    sudo docker-compose up -d db
    sleep 20  # 컨테이너가 완전히 시작될 때까지 대기
fi




echo "> 현재 시간: $(date)" >> $LOG_PATH/deploy.log

docker stop web-server-app
docker rm web-server-app
docker rmi sussa1933/calc-solution:latest



# 기존 MySQL 컨테이너를 중지하고 제거
if [ "$(sudo docker ps -q -f name=$MYSQL_CONTAINER_NAME)" ]; then
    echo "Stopping existing MySQL container..."
    sudo docker stop $MYSQL_CONTAINER_NAME
    echo "Removing existing MySQL container..."
    sudo docker rm $MYSQL_CONTAINER_NAME
fi



NGINX_CONTAINER_NAME=nginx_server
# 기존 Nginx 컨테이너를 중지하고 제거
if [ "$(sudo docker ps -q -f name=$NGINX_CONTAINER_NAME)" ]; then
    echo "Stopping existing NGINX container..."
    sudo docker stop $NGINX_CONTAINER_NAME
    echo "Removing existing NGINX container..."
    sudo docker rm $NGINX_CONTAINER_NAME
fi

CERBOT_CONTAINER_NAME=cerbot
# 기존 Nginx 컨테이너를 중지하고 제거
if [ "$(sudo docker ps -q -f name=$CERBOT_CONTAINER_NAME)" ]; then
    echo "Stopping existing CERBOT container..."
    sudo docker stop $CERBOT_CONTAINER_NAME
    echo "Removing existing CERBOT container..."
    sudo docker rm $CERBOT_CONTAINER_NAME
fi

echo "> Dev DEPLOY_JAR 배포" >> $LOG_PATH/deploy.log

# Docker Compose 빌드
sudo docker-compose build

# 나머지 컨테이너를 Docker Compose을 사용하여 실행
sudo docker-compose up -d