# 가동중인 레디스 컨테이너 중단 및 삭제
sudo docker ps -a -q --filter "name=redis-server" | grep -q . && docker stop redis-server && docker rm redis-server | true
sudo docker ps -a -q --filter "name=redis-master" | grep -q . && docker stop ubuntu_redis-master && docker rm ubuntu_redis-network | true
sudo docker ps -a -q --filter "name=redis-slave" | grep -q . && docker stop ubuntu_redis-slave && docker rm ubuntu_redis-network | true

# 가동중인 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=ewhaproject" | grep -q . && docker stop ewhaproject && docker rm ewhaproject | true

# 레디스 컨테이너 run
#sudo docker-compose up -d

# 기존 이미지 삭제
sudo docker rmi gnidinger/ewha_project:1.0

# 기존 이미지 삭제
sudo docker rmi gnidinger/ewha_project:1.0

# 도커허브 이미지 pull
sudo docker pull gnidingerer/ewha_project:1.0

suudo docker-compose up -d

# 도커 run
#docker run -d -p 8080:8080 -v /home/ubuntu:/config --name ewhaproject gnidinger/ewha_project:1.0

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않음
docker rmi -f $(docker images -f "dangling=true" -q) || true
