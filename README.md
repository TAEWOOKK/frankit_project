## 1.프로젝트 설명

해당 템플릿은 **도메인 기반 설계(Domain-Oriented Design)**와 모듈 분리를 기반으로 구성되어 있으며, 
JPA의 **더티 체킹(Dirty Checking)** 기능을 활용하여 엔티티의 필드 값이 변경되면 자동으로 업데이트가 반영됩니다. 
또한, 부모 엔티티를 저장할 때 연관된 자식 엔티티들도 함께 저장될 수 있도록 JPA의 **연관관계 매핑 및 cascade 설정**을 적극 활용하고 있습니다.

Swagger 문서에서는 API 사용성을 높이기 위해 Enum 타입을 문서화할 수 있도록 전역 설정 메서드를 정의하여, 각 Enum 항목의 의미를 명확하게 확인할 수 있도록 지원합니다.
이로 인해 프론트엔드 및 외부 사용자들이 API 명세를 보다 쉽게 이해할 수 있습니다.
## 2.사용 기술 및 구조

- Java 23
- SpringBoot 3.3.2
- **application**
    - **모듈 설명** : 애플리케이션 구현 로직들이 위치합니다. application 모듈에는 컨트롤러 클래스와 애플리케이션 서비스가 위치합니다. 애플리케이션 서비스는 도메인 서비스를 주입 받아서 도메인
      로직을 호출하거나 도메인을 select하여 도메인 로직을 호출하는 역할을 합니다. 애플리케이션 서비스에는 도메인 로직이 위치하면 안됩니다.
- **configuration**
    - **모듈 설명** : 애플리케이션의 동작을 제어하거나 여러 모듈에서 재사용할 수 있는 설정과 기능들을 모듈로 만듭니다.
    - feign : 외부호출을 위한 feign 클라이언트의 설정을 담당 모듈
    - persistence : jpa 설정 및 db 관련 설정 클래스, yml에는 db연결정보가 존재합니다.
    - security : 소셜 로그인 기능과 JWT 토큰 관련 기능을 담당 모듈
    - springdoc : API 문서화를 위한 설정 담당 모듈
    - web-common : 스프링 웹 애플리케이션 개발을 위한 공통 로직이 들어가있습니다. CORS 설정과 전역 예외 처리를 위한 GlobalExceptionHandler 클래스가 존재합니다.
- **domain**
    - **모듈 설명 :** 핵심 도메인 로직을 갖는 모듈로 jpa를 활용하여 구현합니다.
    - **도메인 모듈별 분리 :** 도메인 별로 모듈을 관리합니다. (ex) 강의 플랫폼과 관련된 도메인의 경우 lecture 모듈을 신규로 생성하여 구현
    - **도메인 모듈간 의존성 :** 도메인 모듈간에는 common 모듈을 제외하고는 서로 참조하지 않도록합니다.
- **module-java**
    - **모듈 설명 :** 순수 자바 클래스로 이루어져 있으며 프로젝트 전반에서 사용합니다.

## 3. 전역 에러 처리

- GlobalExceptionHandler (RestControllerAdvice)
    - 전역적으로 발생하는 예외를 GlobalExceptionHandler에서 처리합니다. 오류 메세지는 동일한 형태로 반환할 수 있도록 “ErrorResponse” 클래스로 관리합니다.
    - type오류의 경우 HttpMessageNotReadableException 예외를 처리하여 [messages.properties](http://messages.properties)에 작성해둔 code
      기반으로 예외 메세지를 반환합니다.
        - (ex) `{0}에 잘못된 타입을 입력하셨습니다. {1} 형으로 입력해주세요.`
    - Bean Validation, MessageSource 기능을 이용하여 요청 값을 검증 후 예외 메세지를 반환합니다.
        - message.properties에 다음과 같이 에러 메세지를 관리합니다.
        - (ex) `NotNull={0}은(는) 필수 입력 값 입니다.`
- 비즈니스 수행 중 던지는 예외는 ErrorType Enum에 에러메세지와 에러코드를 관리합니다.

### swagger 접속 경로

프론트와 통신을 위한 API 문서는 Swagger를 통해 작성합니다. <br>
로컬 Swagger 문서 접속 URI : http://localhost:8080/swagger-ui/index.html

### 환경 변수

-Dspring.profiles.active=local<br>
-Dspring.datasource.url=jdbc:postgresql://localhost:5432/test?serverTimezone=Asia/Seoul&characterEncoding=UTF-8<br>
-Dspring.datasource.username=kto5294<br>
-Dspring.datasource.password=qwer1234<br>
-Dtoken.secret=kimtaewookkJwtTokenSercet!kimtaewookkJwtTokenSercet!kimtaewookkJwtTokenSercet!<br>
-Dcrypt.algorithm=AES<br>
-Dcrypt.transformation=AES<br>
-Dcrypt.key=1234567890123456



