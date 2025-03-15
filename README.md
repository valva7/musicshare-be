### 25/02/21

### 기본 환경

- JDK 17
- Spring Boot 3.3.1
- JPA
- MySQL
- brew install ffmpeg
- brew instal aubio
- brew opencv

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
