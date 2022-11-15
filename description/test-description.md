## 테스트 코드 작성 모범 사례

아래의 모범 사례들은 프로젝트 코드를 살펴보고 어떻게 작성되었는지 확인할 것을 권장한다.

단위 테스트 작성 방법

- 모든 단위 테스트는 Spring Context에 의존하지 않고 테스트를 작성한다.
- 외부 API 호출에 대한 응답 값을 Stubbing하고 싶다면, WireMock, MockServer와 같은 의존성을 추가하여 작성한다.
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
- 통합 테스트 작성 시 **IntegrationSpecHelper**를 상속 받아서 테스트를 작성해야만 한다.
- 통합 테스트는 실제 클라이언트가 동작하는 것과 유사해야 하므로, TestRestTemplate을 사용하여 실제 API Call을 사용하여 테스트하는 것이 바람직하다.
- 통합 테스트 작성 사례는 ```/src/test/kotlin/com/remember/integration_test/user``` 에서 확인 가능하다.

## 각 테스트 별 권장하는 Kotest Isolation Mode

모든 통합 테스트가 특정 컨택스트에 의존하지 않고, 독립적으로 테스트가 가능해야 하므로 Isolation Mode는 절대 수정하지 않는다.

Isolation Mode를 수정하게 되면, 통합 테스트 상에서 스프링 컨택스트 충돌 문제가 발생할 여지가 매우 높다.

단위 테스트 수준이라면, 병렬 테스트를 위해 Isolation Mode를 수정해도 된다. 하지만 굳이 수정할 필요는 개인적으로 없다고 생각한다.

## MockK 사용 사례

MockK에 대한 사용 사례를 알고 싶다면, playground 모듈 하위 ```/src/test/kotlin/com/remember/playground/mockk```에 존재하는 StudyMockKSpec를 참고하여 작성한다.

## interfaces 계층에 대한 단위 테스트 모범 사례

user-api 모듈 하위 ```/src/test/kotlin/com/remember/unit_test/user/request```에 존재하는 UserRequestSpec을 참고하여, HTTP API에 대한
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
