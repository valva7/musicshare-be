# 1. k8s 환경 구축하기
1. vm 도구 설치 (docker)
   brew install docker
2. kubectl & minikube 설치
   brew install kubectl
   brew install minikube
3. 설치 완료 테스트
   docker desktop 실행   https://www.docker.com/products/docker-desktop/
   minikube start
4. kubectl get all
5. https://hub.docker.com/ 가입 후 image push를 통해 레포지토리 만들기

docker build -t {hub id}/{repo name}:0.0 .
docker push {hub id}/{repo name}:0.0

네임스페이스
├── 파드 1
│    ├── 컨테이너 A
│    ├── 컨테이너 B
│
├── 파드 2
│    ├── 컨테이너 C
│
├── 서비스 (파드들을 묶어 네트워크 제공)

## application build
- ./gradlew clean build

# 2. k8s 커맨드
POD Deploy
- kubectl apply -f app.yaml
  Namespace list
- kubectl get namespace
  All resource info
- kubectl get all
  All service info
- kubectl get service
  POD info
- kubectl get pod mysql-7f97b96ff8-rhvvx
  POD log
- kubectl logs pod/auth-service-9b49dc9d7-9vj59
  POD delete
- kubectl delete pod auth-service-9b49dc9d7-9vj59

# 3.minikube 커맨드
minikube service 터널
- minikube service musicshare

# 4.Docker 커맨드
## Docker Image 관리
- https://hub.dokcer.com (docker hub 사이트)

docker image create after
- docker buildx build --platform linux/arm64 --load -t devkimgleam/auth-service:0.0 .
  image list
- docker images
  image run
- docker run devKimgleam/auth-service: 0.0
  docker image push
- docker push devkimgleam/auth-service:0.0
  image delete
- docker rmi  devkimgleam/auth-service:0.0

## 접속 방법 및 정보
### 모니터링
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
### docker 컨테이너 접속
- Redis: docker exec -it redis redis-cli
- Kafka: docker exec -it kafka /bin/bash