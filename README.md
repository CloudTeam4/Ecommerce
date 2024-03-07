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
- 테스트 코드 작성에 익숙해지기
- Kubernetes 맛보기

### ✅ 인프라 구축

- Docker :
- Github Actions :
</aside>

</br></br>

## ⚒️ [ Architecture ]
### 선착순 쿠폰 발급 이벤트
<img width="775" alt="스크린샷 2024-03-07 오후 3 50 13" src="https://github.com/CloudTeam4/Ecommerce/assets/68779402/20f2357e-2e8a-40dd-9ab4-184f808d960e">
