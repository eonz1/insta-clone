# insta-clone

인스타그램 클론 코딩 백엔드 프로젝트
---

## 커밋 컨벤션
- Java17
- MySQL8
```
/* mysql5.7 사용 시, function 추가 */

CREATE FUNCTION BIN_TO_UUID(b BINARY(16))
RETURNS CHAR(36)
BEGIN
   DECLARE hexStr CHAR(32);
   SET hexStr = HEX(b);
   RETURN LOWER(CONCAT(
        SUBSTR(hexStr, 1, 8), '-',
        SUBSTR(hexStr, 9, 4), '-',
        SUBSTR(hexStr, 13, 4), '-',
        SUBSTR(hexStr, 17, 4), '-',
        SUBSTR(hexStr, 21)
    ));
END
```

## Configuration

application.yml
```
spring:
    datasource:
        url:
        username:
        password:
        
letter:
    id:
    pwd:

ncloud:
    api:
        accessKeyId:
        secretAccessKey:
        
jwt:
    secret-key:
```

위에 적힌 항목들을 환경에 맞춰 수정해야 합니다.

## Installation

### 1. Gradle
```
./gradlew bootBuildImage
```

### 2. Dockerfile
```
./gradlew bootJar
```
```
// 플랫폼은 환경에 맞춰 수정합니다.

docker build --platform=linux/amd64 -t prom:0.0.1-SNAPSHOT ./
```

## Usage

### 1. java
```
cd /prom/build/libs

java -jar prom-0.0.1-SNAPSHOT.jar
```

### 2. docker-compose
```
docker-compose -d up
```

## Commit Message Convention

|    prefix    | description                                                            |
| :----------: | ---------------------------------------------------------------------- |
|     Feat     | 새로운 기능 추가                                                           |
|     Fix      | 버그 수정                                                                |
|   Refactor   | 리팩토링                                                                 |
|    Design    | CSS 등 사용자 UI 디자인 변경                                                |
|   Comment    | 필요한 주석 추가 및 변경                                                    |
|    Style     | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우                                 |
|     Test     | 테스트 코드 추가, 수정, 삭제, 비즈니스 로직에 변경이 없는 경우                       |
|    Chore     | 위의 걸리지 안는 기타 변경사항(빌드 스크립트 수정, assets image, 패키지 매니저 등)    |
|     Init     | 프로젝트 초기 생성                                                         |
|    Rename    | 파일 혹은 폴더명을 수정하거나 옮기는 경우                                        |
|    Remove    | 파일을 삭제하는 작업만 수행한 경우                                             |
|     Docs     | 문서 추가/수정                                                            |

## 커밋 규칙

1. 제목은 최대 50글자 넘지 않기
2. 마침표 및 특수기호 사용x
3. 첫 글자 대문자, 과거시제 사용x, 명령어 사용
4. 본문에 '무엇을', '왜' 변경했는지에 최대한 상세히 작성
5. 소스코드를 보지 않고도 변경 사항이 무엇을 하는지 알 수 있도록 하기
