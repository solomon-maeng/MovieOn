# MovieOn API

MovieOn API는 다음 기술로 개발되었습니다.

- Kotlin 1.7.10
- SpringBoot 2.7.5
- Spring 5.3.23
- MySQL 8.0.30

## 실행 방법

프로젝트 실행 전 다음을 가정한다.

- Docker가 설치되어있다고 가정
- M1 Pro Mac을 사용 중이라고 가정
- JDK 17, Intellij 설치가 되었다고 가정

로컬 실행

1. 프로젝트 Clone
2. 프로젝트 루트 디렉토리에서 ```$ ./gradlew clean build``` 로 빌드 및 테스트 실행
3. 프로젝트 하위 docker/mysql/에서 ```$ docker-compose up -d``` 실행
4. 프로젝트 루트로 돌아와서 ```$ java -jar -Dspring.profiles.active=local server/build/libs/server-0.0.1.jar```
   실행

## 코드 아키텍처

모듈형 모노리스 아키텍처를 사용하여, 다음 모듈들로 프로젝트가 구성되어 있음.

| 모듈               | 설명                                          |
|------------------|---------------------------------------------|
| server           | 각 도메인 API 모듈을 통합하고 실행하는 모듈. 배포 시, 이 모듈을 사용. |
| shared           | 모든 모듈에서 의존하는 공통적인 의존성을 담은 모듈.               |
| order-api        | 주문을 생성하고, 주문을 관리 및 처리하는 기능을 담당하는 모듈.        |
| user-api         | 인증, 인가, 사용자 관리 기능을 담당하는 모듈.                 |
| product-api      | 상품을 관리하고 사용자에게 노출하는 기능을 담당하는 모듈.            |
| payment-api      | 주문한 상품에 대한 결제 처리 및 취소를 담당하는 모듈.             |
| notification-api | 이메일, 푸시 등의 알림을 사용자에게 발송하고 알림 설정을 관리하는 모듈.   |
| query-api        | 복잡한 조회와 관련한 요구사항을 처리하는 모듈(CQRS).            |
| batch-api        | 배치 처리와 관련한 요구사항을 처리하는 모듈.                   |

각 도메인 API 모듈은 다음과 같은 하위 모듈로 구성되어 있음.
- 각 하위 모듈들은 계층형 아키텍처 형식을 따르며, interfaces를 기준으로 바로 ***아래 모듈만*** 의존해야 한다.
- Domain 모듈은 다른 어떠한 모듈도 의존하면 안된다. 다른 모듈이 Domain 모듈을 의존해야 한다.
- 예외적으로 infrastructure 모듈만 다른 모든 모듈을 의존할 수 있다.
- 예외적으로 특정 모듈은 하위 모듈 구성을 하지 않을 수 있고 혹은 다른 아키텍처 구조를 가져갈 수도 있다.

| 하위 모듈 명             | 설명                                                     |
|---------------------|--------------------------------------------------------|
| user-interfaces     | 유저 HTTP API에 대한 진입점을 제공하는 모듈.                          |
| user-application    | 유저의 비즈니스 로직 제어를 담당하는 모듈.                               |
| user-domain         | 유저의 비즈니스 로직이 담긴 핵심 모듈.                                 |
| user-infrastructure | 외부 구성요소, 프레임워크, 영속성을 DIP를 사용하여 역전시키고 동작가능하도록 하는 기반 모듈. |

## 테스트 코드 작성 모범 사례

아래의 모범 사례들은 프로젝트 코드를 살펴보고 어떻게 작성되었는지 확인할 것을 권장한다.

단위 테스트 작성 방법
- 모든 단위 테스트는 Spring Context에 의존하지 않고 테스트를 작성한다.
- 외부 API 호출에 대한 응답 값을 Stubbing하고 싶다면, WireMock 의존성을 추가하여 작성한다.
- 데이터베이스 접근을 추상화한 Repository 인터페이스는 Test Double 중 Fake 객체를 직접 구현하여 사용한다.

단위 테스트에서 Test Double(Dummy, Stub, Fake, Spy, Mock)을 사용해야만 하는 경우는 아래와 같다.
- 테스트 대상 시스템이 의존하는 객체가 구현체가 있지 않은 경우 Test Double을 사용하여 테스트를 작성한다.
- 외부 협력사 API, 클라우드 플랫폼 API 등 언제라도 잘못된 동작이 발생할 여지가 있는 경우는 Test Double을 사용하여 테스트를 작성한다.
- 테스트 대상 시스템이 의존하는 객체를 사용하여 테스트를 진행했을 때, 테스트 실행 시간이 수백 밀리초에서 수 초가 소요될 경우 Test Double을 사용하여 테스트를 작성한다.
- 정의된 인터페이스가 단순하다면, Test Double이 만드는 가정은 작아지기 때문에 테스트 신뢰도와 안정감을 훼손시키지 않으므로 Test Double을 사용하여 테스트를 작성한다.

통합 테스트 작성 방법
- 통합 테스트는 Server 모듈에 존재하는 test 디렉터리에서만 작성 및 실행한다.
- 통합 테스트 작성 시 ```@SpringBootTest```가 붙어있는 최상위 테스트 구성 클래스를 상속 받아 작성한다.
    - 최상위 테스트 구성 클래스에만 ```@MockBean``` 을 사용할 것을 강력히 권장한다.
    - 만약, ```@DataJpaTest```같은 다른 Slice 테스트를 사용하고 싶은 경우, 테스트 루트 디렉토리에서 기존 통합 테스트 디렉터리와 완전히 분리하여 작성할 것을 권장한다. 또한 해당 ```@DataJpaTest```가 붙은 최상위 구성 클래스를 사용하여 테스트를 작성할 것을 권장한다.
- 다른 테스트의 실행을 충돌시키지 않도록, 기본 값 설정은 반드시 테스트 실행 전에 실행되도록 하고, 테스트 실행 후에는 TRUNCATE문을 사용하여 비워주거나, DELETE문을 사용하여 명시적으로 데이터베이스에서 삭제되도록 할 것을 권장한다.
