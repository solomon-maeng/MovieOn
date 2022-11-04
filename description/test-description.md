## 테스트 코드 작성 모범 사례

아래의 모범 사례들은 프로젝트 코드를 살펴보고 어떻게 작성되었는지 확인할 것을 권장한다.

단위 테스트 작성 방법

- 모든 단위 테스트는 Spring Context에 의존하지 않고 테스트를 작성한다.
- 외부 API 호출에 대한 응답 값을 Stubbing하고 싶다면, WireMock 의존성을 추가하여 작성한다.
- 데이터베이스 접근을 추상화한 Repository 인터페이스는 Test Double 중 Fake 객체를 직접 구현하여 사용한다.

단위 테스트에서 Test Double(Dummy, Stub, Fake, Spy, Mock)을 사용해야만 하는 경우는 아래와 같다.

- 테스트 대상 시스템이 의존하는 객체가 구현체가 있지 않은 경우 Test Double을 사용하여 테스트를 작성한다.
- 외부 협력사 API, 클라우드 플랫폼 API 등 언제라도 잘못된 동작이 발생할 여지가 있는 경우는 Test Double을 사용하여 테스트를 작성한다.
- 테스트 대상 시스템이 의존하는 객체를 사용하여 테스트를 진행했을 때, 테스트 실행 시간이 수백 밀리초에서 수 초가 소요될 경우 Test Double을 사용하여 테스트를
  작성한다.
- 정의된 인터페이스가 단순하다면, Test Double이 만드는 가정은 작아지기 때문에 테스트 신뢰도와 안정감을 훼손시키지 않으므로 Test Double을 사용하여 테스트를
  작성한다.

통합 테스트 작성 방법

- 통합 테스트는 Server 모듈에 존재하는 test 디렉터리에서만 작성 및 실행한다.
- 통합 테스트 작성 시 ```@SpringBootTest```가 붙어있는 최상위 테스트 구성 클래스를 상속 받아 작성한다.
    - 최상위 테스트 구성 클래스에만 ```@MockBean``` 을 사용할 것을 강력히 권장한다.
    - 만약, ```@DataJpaTest```같은 다른 Slice 테스트를 사용하고 싶은 경우, 테스트 루트 디렉토리에서 기존 통합 테스트 디렉터리와 완전히 분리하여 작성할
      것을 권장한다. 또한 해당 ```@DataJpaTest```가 붙은 최상위 구성 클래스를 사용하여 테스트를 작성할 것을 권장한다.
- 다른 테스트의 실행을 충돌시키지 않도록, 기본 값 설정은 반드시 테스트 실행 전에 실행되도록 하고, 테스트 실행 후에는 TRUNCATE문을 사용하여 비워주거나, DELETE문을
  사용하여 명시적으로 데이터베이스에서 삭제되도록 할 것을 권장한다.

## 각 테스트 별 권장하는 Kotest Isolation Mode

기본적으로 Domain 계층에 대한 테스트는 기본 값인 SingleInstance Mode를 사용할 것을 권장한다.

- SingleInstance Mode를 사용해도 좋은 이유는, 외부 인프라, Spring에 의존하지 않은 순수한 POKO 테스트이기 때문이다.

다음과 같이, 통합 테스트를 작성해야 하는 경우는 InstancePerLeaf Mode를 사용할 것을 권장한다.

- 다음과 같이 작성하면, describe 컨택스트 별로 한번 씩만 테스트 인스턴스를 생성한다. 따라서 총 2번 생성된다.

```kotlin
describe() {
    context() {
        it() {

        }
    }
}

describe() {
    context() {
        it() {

        }
    }
}
```

마지막으로, InstancePerTest는 내부 컨택스트를 포함해서 모든 테스트에 대한 인스턴스를 재생성하기 때문에 테스트 속도 및 비용 측면에서 권장되지 않는다.

## 각 테스트 별 사용해야 하는 Spec 애노테이션

Spec 애노테이션은 server 모듈에만 존재하며, 통합 테스트를 위해서만 사용하는 메타 애노테이션이다.

사용 가능한 애노테이션은 다음과 같다.
- ```@ApiSpec``` MockMvc를 실제 톰캣 컨테이너로 띄워서 HTTP API를 테스트할 때 사용한다. 가장 넓은 범위의 블랙 박스 테스트이다.
- ```@IntegrationSpec``` API, Repository 그 어느 곳에 속하지 않고, 그 외의 인프라나 Facade 계층을 테스트할 때 사용한다.
- ```@RepositorySpec``` Repository의 쿼리 결과가 기대한 대로 동작하는지 테스트할 때 사용한다.

## interfaces 계층에 대한 단위 테스트 모범 사례

user-api 모듈 하위 ```/src/test/com/remember/user/interfaces```에 존재하는 UserRequestSpec을 참고하여, HTTP API에 대한
입력 값 검증 테스트를 작성할 것을 권장한다.

아래의 코드처럼 withData() 안에 data class를 넣어주면, 다음과 같이 3개의 케이스에 대해 중복을 제거할 수 있고 깔끔하게 테스트가 가능하다.

```kotlin
context("회원가입 요청 시, 입력 값이 잘못된 경우 400 BadRequest 를 반환한다.") {
    withData(
        RegisterUserRequest("", "", ""),
        RegisterUserRequest("kitty", "aaa123", "1234567!@"),
        RegisterUserRequest("kitty", "kitty123@gmail.com", "12345"),
    ) { request ->
        mockMvc.post(REGISTER_URI) {
            jsonBody(request)
        }.andDo {
            print()
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
```
