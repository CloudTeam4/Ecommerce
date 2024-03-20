# [모쿠몰]

## ✍️ 프로젝트 소개

<aside>
💡 “모쿠몰” 프로젝트는 이커머스 기반의 프로젝트로 이커머스에서 자주 확인할 수 있는 이벤트, 선착순 쿠폰 등의 기능을 개발하면서 기술적 도입 및 숙련도를 높이기 위한 프로젝트 입니다.
    
</br>

### ✅ 프로젝트 핵심 기능

- RabbitMQ, Redis → 선착순 쿠폰 발급 API ( 데이터 유실 방지, 부하 분산, 동시성 이슈 방지 )
    - [ 선착순 이벤트 요구사항 ]
        - 쿠폰의 지급 수량은 당일 정해진 양을 **초과**해서는 안된다.
        - 쿠폰은 **1인당 1장**만 지급한다.
        - 쿠폰의 수량보다 더 많은 요청이 왔을 때, 선착순 인원을 제외한 나머지 인원에게 실패 메세지 전달한다.
- Spring Batch → Batch를 활용하여 특정 날짜와 시간에 오픈되는 이벤트 API

### ✅ 프로젝트 목표

- Spring, RabbitMQ, Redis, Spring Batch, CI / CD + Docker 숙련도 높이기
- 테스트 코드를 기반으로 개발하는 것에 익숙해지기

### ✅ 인프라 구축

- AWS EC2 → 4대
    - Spring Application → Docker로 띄우기
    - Spring ( Consumer ) → Docker로 띄우기
    - RabbitMQ → 서버에서 직접 실행
    - Redis → 서버에서 직접 실행
    
    ➡️ 하나의 서버에 하나의 컨테이너만 띄우는데 굳이 Docker를 사용할 이유가 없다고 생각했습니다. 그래서 RabbitMQ와 Redis는 서버에서 직접 실행을 하기로 했고 Spring도 JAR 파일을 바로 실행하면 되는 것이지만 프로젝트의 목표 중 하나가 Docker에 대한 숙련도를 높이는 것이기에 Spring은 Docker를 사용하여 띄우기로 하였습니다.
    
- Github Actions : github action 의 상위 개념인 Workflow의 yml 파일과 DockerFile을 통한 CI / CD 파이프라인 구축.
</aside>

</br></br>

## ⚒️ [ Architecture ]
### 선착순 쿠폰 발급 이벤트
<img width="775" alt="스크린샷 2024-03-07 오후 3 50 13" src="https://github.com/CloudTeam4/Ecommerce/assets/68779402/20f2357e-2e8a-40dd-9ab4-184f808d960e"> </br>
### 특가상품 이벤트 생성
<img width="775" alt="스크린샷 2024-03-07 오후 3 50 13" src="https://github.com/CloudTeam4/Ecommerce/assets/77678677/ab8171e0-b4f2-4efc-bc83-ef46c6354a6d"></br>
### 특정 회원들에게 쿠폰 발급
<img width="775" alt="스크린샷 2024-03-07 오후 3 50 13" src="https://github.com/CloudTeam4/Ecommerce/assets/77678677/a7703786-2c75-4354-8ce8-5a2588bd4687"></br>

### CI / CD
<img width="879" alt="스크린샷 2024-03-18 오후 2 19 55" src="https://github.com/CloudTeam4/Ecommerce/assets/68779402/0befb141-cc78-40ff-8e22-935ae72f1c01">
