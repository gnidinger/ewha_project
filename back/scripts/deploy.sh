# 가동중인 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=ewha_project" | grep -q . && docker stop ewhaproject && docker rm ewhaproject | true

# 기존 이미지 삭제
sudo docker rmi gnidigner/ewha_project:1.0

# 도커허브 이미지 pull
sudo docker pull gnidigner/ewha_project:1.0

# 도커 run
docker run -d -p 8080:8080 -v /home/ubuntu:/config --name ewhaproject gnidinger/ewha_project:1.0

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않음
docker rmi -f $(docker images -f "dangling=true" -q) || true
