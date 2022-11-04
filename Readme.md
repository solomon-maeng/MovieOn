# Kotlin + Spring 온보딩 프로젝트 

## 목차

- [프로젝트 설명](description/project-description.md)
- [테스트 모범 사례 설명](description/test-description.md)

## 실행 방법

프로젝트 실행 전 다음을 가정한다.

- Docker가 설치되어있다고 가정
- M1 Pro Mac을 사용 중이라고 가정
- JDK 17, Intellij 설치가 되었다고 가정

로컬 실행

```mkdir .git/hooks``` 실행 시, 이미 .git/hooks가 존재하면 패스하고 다음 명령어 실행.

1. ```./gradlew ktlintApplyToIdea``` 실행
2. ```mkdir .git/hooks``` 실행
3. ```./gradlew addKtlintCheckGitPreCommitHook``` 실행
4. ```./gradlew clean build``` 로 빌드 및 테스트 실행
5. ```cd docker/mysql``` 실행
6. ```docker-compose up -d``` 실행
7. ```java -jar -Dspring.profiles.active=local server/build/libs/server-0.0.1.jar``` 실행

애플리케이션이 실행 중이라면, ```http://localhost:8081/api/v1/swagger``` 로 이동.
