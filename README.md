# 🎵 프로젝트 개요 – 음악 공유 플랫폼

## 🔍 프로젝트 목적
개인이 작곡한 음악을 자유롭게 업로드하고, 다른 사용자들과 감상 및 공유할 수 있는 **커뮤니티 중심의 음악 플랫폼**을 구축합니다.

---

## ✨ 주요 기능 및 특징

### 1. 🎧 음악 업로드 및 관리
- 사용자는 자신이 제작한 음악 파일을 업로드할 수 있습니다.
- 업로드된 음악 파일은 **AWS S3**에 저장되며, 해당 파일 경로를 서버에 전달해 **DB에 저장 및 관리**합니다.

### 2. 🧠 음악 자동 분석 및 AI 기반 분위기 추천
- 업로드된 음악은 자동으로 **BPM(비트)**, **주파수** 등의 음향 특성이 분석됩니다.
- 분석된 정보를 기반으로, ChatGPT AI가 음악의 분위기(예: 차분함, 역동적 등)**를 판단하고 태깅합니다.

### 3. 🤝 커뮤니티 기능
- 사용자들은 음악에 대해 **좋아요**와 **댓글**을 남길 수 있습니다.
- 아티스트를 **Fanning(팬 등록)** 할 수 있으며, 팬 수에 따른 인기 순위 제공(예정)이 가능합니다.
- 좋아요 또는 팬 등록 시 해당 아티스트에게 **FCM Web Push 알림**이 발송됩니다.
- **숏츠(Shorts)** 형식의 음악 추천 기능을 통해 알고리즘 기반으로 랜덤 재생이 가능합니다.(예정)

### 4. 🔐 인증 및 보안 (JWT 기반)
- **카카오 소셜 로그인**을 통한 간편 인증을 지원합니다.
- 별도의 **이메일 기반 자체 회원가입** 기능도 제공합니다.
- 인증은 **JWT(Json Web Token)** 기반으로 안전하게 처리됩니다.

---

## 🔮 추후 추가 예정 기능

### 1. 📈 인기 차트 및 랭킹
- 좋아요 수, 조회 수, 댓글 수 등을 기반으로 실시간 음악 인기 차트 제공
- 테마별, 장르별 랭킹 분류 기능

### 2. 🔍 고도화된 검색 및 필터링
- 제목, 아티스트, 태그, 장르, 분위기 등 다양한 조건으로 음악 검색 가능
- 최신순, 인기순, 랜덤 등의 정렬 기능

### 3. 📼 숏츠 기반 음악 피드 (Shorts Music Feed)
- 틱톡/릴스처럼 짧은 미리 듣기 형식의 랜덤 음악 피드 제공
- 사용자의 좋아요/재생 이력을 기반으로 한 개인화 추천 알고리즘 도입

### 4. 💬 실시간 댓글 및 반응 기능
- 음악 재생 중 실시간 댓글 표시 (유튜브 라이브 채팅 형식)
- 하트, 불꽃 등 감정 표현 이모지 반응 기능

### 5. 🧑‍🤝‍🧑 아티스트 페이지/프로필 강화
- 아티스트 개별 페이지에서 등록 음악, 팬 수, 소개글 등을 확인 가능
- 팬 간 커뮤니티 기능(게시판, 방명록 등) 추가 고려

### 6. 📢 알림 및 활동 피드
- 좋아요, 댓글, 팬 등록 등의 활동에 대한 마이페이지 알림 탭 제공
- 실시간 또는 일정 간격으로 푸시 알림 요약 전송

### 7. 💸 음악 수익화 연동 (장기 목표)
- 스트리밍 횟수에 따른 수익 정산 시스템 구축
- 후원하기(투네이션 스타일), 음원 판매 등도 고려


## ⚙️ 개발 환경

- **Version Control**: GitHub
- **Backend**: Java, Spring Boot, Spring Security, JPA
- **Frontend**: React.js, Next.js, TailwindCSS
- **Database**: MySQL, Redis
- **Storage**: AWS S3
- **Authentication**: Kakao OAuth, JWT
- **Notification**: Firebase Cloud Messaging (FCM)
- **Message Queue**: Kafka
- **Containerization**: Docker, DockerHub
- **Orchestration**: Kubernetes(Minikube)
- **Deployment(추후)**: ArgoCD, GitHub Actions
- **Monitoring**: Prometheus, Grafana
- **음악 분석**: ffmpeg, aubio, opencv
- **AI 분석**: ChatGPT
```
- brew install ffmpeg
- brew instal aubio
- brew opencv
```


## K8S 환경 구축하기
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


# application build
- ./gradlew clean build

# k8s 커맨드
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

# Docker 커맨드
## https://hub.dokcer.com (docker hub 사이트)
docker image create
- docker buildx build --platform linux/arm64 --load -t devkimgleam/auth-service:0.0 .
image list
- docker images
image run
- docker run devKimgleam/auth-service: 0.0
docker image push
- docker push devkimgleam/auth-service:0.0
image delete
- docker rmi  devkimgleam/auth-service:0.0


# minikube 커맨드
minikube service 터널
- minikube service musicshare


## 접속 정보
### 모니터링
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
### docker 컨테이너 접속
- Redis: docker exec -it redis redis-cli
- Kafka: docker exec -it kafka /bin/bash


### 프로젝트 실행

- 로컬 실행

  > ### <Java version 변경>
  >
  > 1.프로젝트 우클릭 > 모듈 설정 > 프로젝트
  >
  > - SDK : corretto-17 Amazon Corretto
  >
  > ### <인텔리제이 빌드 및 컴파일 설정>
  >
  > 1.인텔리제이 설정 > 빌드,실행,배포 > 빌드 도구 > Gradle 프로젝트
  >
  > - IntelliJ IDEA
  >
  > 2.빌드,실행,배포 > Java 컴파일러
  >
  > - 17
  >
  > ### <Gradle 로드>
  >
  > 1.Gradle 로드로 라이브러리 설치
  >
  > ### <어플리케이션 설정>
  >
  > 1.Spring Boot 어플리케이션 생성
  >
  > 2.빌드 및 실행 : java 17
  >

### 실행

- Base Url : http://localhost:8080

### Git Commit 메시지 컨벤션

> - Feat : 새로운 기능을 추가하는 경우
> - Fix : 버그를 고친경우
> - Docs : 문서를 수정한 경우
> - Style : 코드 포맷 변경, 세미콜론 누락, 코드 수정이 없는경우
> - Refactor : 코드 리펙토링
> - Test : 테스트 코드. 리펙토링 테스트 코드를 추가했을 때
> - Chore : 빌드 업무 수정, 패키지 매니저 수정
> - Design : CSS 등 사용자가 UI 디자인을 변경했을 때
> - Rename : 파일명(or 폴더명) 을 수정한 경우
> - Remove : 코드(파일) 의 삭제가 있을 때. "Clean", "Eliminate" 를 사용하기도 함
> - #### ex) Feat : 로그인 기능 추가

### Coding Check Style 설정 (Google)

- 사전 준비 설정 파일 (배포)

> intellij-java-google-style

- 설정 방법

> 1.Check Style 플러그인 설치
>
> 2.인텔리제이 설정 > 도구(Tool) > Check style
> 3.에디터(Editor) > 코드스타일(Code Style)
>
> - 구성표 > 구성표 가져오기 > Checkstyle configuration > intellij-java-google-style 파일 추가 > 구성표 변경
>
> 4.단축키로 Formatter 적용
>
> - 코드 서식 적용 : Opt + Command + L
> - import문 최적화 : Ctrl+ Opt + O

### 주석 템플릿 적용

#### Class 주석

> 1.인텔리제이 설정 > 에디터 > 파일 및 코드 템플릿
>
> - "포함" 탭 > File Header > 아래 설정값 입력

```java
/**
 * ${PACKAGE_NAME}.${NAME}
 * <p>
 * ${NAME}
 *
 * @author 김태욱
 * @version 1.0
 * @since ${YEAR}/${MONTH}/${DAY}
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  ${YEAR}/${MONTH}/${DAY}    김태욱            최초 생성
 * </pre>
 */
```

#### Method 주석

> 1.인텔리제이 설정 > 에디터 > 파일 및 코드 템플릿
>
> - "포함" 탭 > `+` 클릭 > Method Header 생성 (아무것도 입력할 필요 없음)
> - "코드" 탭 > JavaDoc Method > 아래 설정값 입력

```
#parse("Method Header.java")
#foreach($param in $PARAMS)
 *@param $param
#end
#if($RETURN_TYPE !="void")
 *@return
#end
```

> 2.사용법
>
> - 메소드 생성 > 메소드 상단에 /** 입력 후 엔터

# ======================== 개발 가이드 ========================

## 1. 미정

- 미정

```미정
```

> - 미정

- @RequestParam

```java
코드
```
