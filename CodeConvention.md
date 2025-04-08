# 📘 코드 컨벤션 (MD)

## 1. 클래스/파일 구조
```java
// 순서대로 선언
package com.example.app;
import ...
/**
 * 클래스 설명 Javadoc
 */
public class MyService {
    // 상수
    public static final int MAX_SIZE = 100;

    // 필드
    private final UserRepository userRepository;

    // 생성자
    public MyService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // public 메서드
    public void doSomething() {
        // ...
    }

    // private 메서드
    private String formatName(String name) {
        return name.trim().toUpperCase();
    }
}
```

---

## 2. 어노테이션 사용 규칙 및 메서드 네이밍
### 접두사 정리
- 속성에 접근: get/set
- 데이터 생성: create
- 데이터 조회: find
- 데이터 변경: modify
- 데이터 삭제: delete
- 데이터 입력: input
- 데이터 초기화: init
- 데이터 불러오기: load
- 데이터 유무 확인: has

### Controller 클래스
- `@Tag`, `@RequestMapping`, `@RestController`, `@RequiredArgsConstructor`
- Controller: HTTP 액션 + 리소스명 중심 (ex. `getUser`, `createUser`, `updateUser`)
```java
@Tag(name = "Music", description = "음악 관련 API")
@RequestMapping("/music/public")
@RestController
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @GetMapping("/{id}")
    public Response<MusicRes> getMusic(@PathVariable Long id) {
        return Response.ok(musicService.getMusic(id));
    }
}
```

### Service
- `@Slf4j`, `@Service`, `@RequiredArgsConstructor`
- Service: 비즈니스 동작 설명 (ex. `findUserById`, `registerUser`, `modifyUserInfo`)
- 비즈니스 로직은 이곳에서 처리하며, 트랜잭션 처리 필요시 명시적으로 지정
```java
@Slf4j
@Service
@RequiredArgsConstructor
public class MusicService {
    private final Musicepository musicRepository;

    @Transactional(readOnly = true)
    public MusicRes getMusic(Long id) {
        // ...
    }
}
```

### Repository
- Spring Data JPA 메서드는 도메인 + 조건 + 키워드 기반
    - `findBy`, `countBy`, `existsBy`, `deleteBy` 사용
    - 예시:
        - `findByUsername(String username)`
        - `findByEmailAndStatus(String email, Status status)`
        - `existsByEmail(String email)`
        - `deleteByCreatedDateBefore(LocalDate date)`
```java
public interface MusicRepository {
    Music findMusicById(Long id);
}
```

### Entity
- `@Entity`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@Table`
- 테이블명 + Entity
- 연관관계는 `@ManyToOne(fetch = LAZY)` 등으로 반드시 명시
- 양방향 관계에서는 연관관계의 주인을 명확히 지정
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "music")
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MusicFile musicFile;
}
```

### VO(Value Object)
- `@Builder`, `@Getter`, `@Setter`, `@AllArgsConstructor`
```java
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Music {
    private Long id;
    private String name;

}
```

### DTO
- `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- ✅ 명명
| 타입 | 접미사 | 예시 |
|------|--------|------|
| 요청 DTO | `Req` | `MemberCreateReq` |
| 응답 DTO | `Res` | `MemberInfoRes` |
```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {
    @NotBlank
    private String username;

    @Email
    private String email;
}
```

---

## 3. 네이밍 규칙
| 항목 | 규칙 | 예시                                  |
|------|------|-------------------------------------|
| 클래스 | UpperCamelCase | MemberController, MemberServiceImpl |
| 변수 | lowerCamelCase | memberId, createdAt                 |
| 상수 | UPPER_SNAKE_CASE | MAX_PAGE_SIZE                       |
| 복수형 변수 | 의미 있는 복수형 | memberList, memberDtos              |
| boolean 변수 | is/has/should + 명사/동사 | isValid, hasPermission              |

---


## 4. 문법 및 작성 규칙
- 들여쓰기: `4칸`
- 중괄호 `{}` 스타일: `K&R (Kernighan and Ritchie)` 스타일 사용
  ```java
  if (user != null) {
      // 처리
  } else {
      // 처리
  }
  ```
- 반복문: Stream을 선호하되 가독성이 떨어지면 일반 for문 사용
```java
// ✅ stream 예시
List<String> names = users.stream()
    .map(User::getName)
    .collect(Collectors.toList());
// ✅ 일반 for문 예시
for (User user : users) {
    log.info(user.getName());
    }
```
- 메서드 간은 `한 줄 띄우기`
- `if`, `for`, `while` 등은 항상 `{}` 사용
- 조건문은 부정보다 긍정을 우선
    - ❌ `if (!isValid)` → ✅ `if (isInvalid)`
- 클래스 내 내부 클래스(Nested Class) 작성 금지 → 별도 파일로 분리

---

## 5. 예외 처리 규칙
- 커스텀 예외 클래스 작성 (ex. `MemberNotFoundException`)
```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberNotFoundException.class)
    public Response handleUserNotFound(MemberNotFoundException ex) {
        return Response.error(HttpStatus.NOT_FOUND);
    }
}
```

---

## 6. 로그 출력 규칙
- `@Slf4j` 어노테이션 사용
- 메시지는 `{}` 형식으로 분리 표현
```java
@Slf4j
@Service
public class MusicService {
    public void register(User user) {
        log.info("사용자 등록 완료: {}", user.getId());
    }

    public void handleInvalidRequest(String url) {
        log.warn("잘못된 접근: {}", url);
    }
}
```

---

## 7. 주석 스타일
### 클래스 위 Javadoc
```java
/*
 * <p>
 * 클래스명
 *
 * @author 김태욱
 * @version 1.0
 * @since 2025/01/01
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  2025/01/01    김태욱            최초 생성
 * </pre>
*/
```
### 메서드 Javadoc
```java
/**
 * 메서드 동작 설명
 *
 * @param id 사용자 ID
 * @return 사용자 정보
 */
```

## 8. REST API 설계 규칙
### 설계 원칙
- URL은 소문자 사용, 동사 지양 (자원 중심 설계)
- 리소스는 **복수형 명사** 사용
- 동작은 HTTP 메서드로 표현 (GET, POST, PUT, DELETE)
- 응답은 통일된 포맷 제공
### URL 예시
| 메서드 | URI | 설명 |
|--------|-----|------|
| GET | /api/users | 전체 사용자 조회 |
| GET | /api/users/{id} | 사용자 단건 조회 |
| POST | /api/users | 사용자 생성 |
| PUT | /api/users/{id} | 사용자 전체 수정 |
| DELETE | /api/users/{id} | 사용자 삭제 |

---

## 9. 응답 포맷
```json
{
  "code": "200",
  "message": "completed!",
  "value": null
}
```



---


