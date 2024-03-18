package com.teamsparta.ecommerce.integrationtest

import org.junit.jupiter.api.BeforeAll
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer


@ActiveProfiles("test") //테스트용 프로파일인 "test"를 활성화, 이 프로파일을 사용하면 테스트용 환경 설정이 로드.
@SpringBootTest(classes = [EcommerceApplication::class])
//@Sql("classpath:/db/init_table.sql") //테스트용 데이터베이스의 초기 데이터를 로드하는데 사용, classpath를 통해 클래스패스 상의 SQL 파일을 지정
@Sql("classpath:/db/dml.sql") //DML 쿼리를 실행하여 초기 데이터베이스 상태를 설정하는데 사용
class IntegrationTest() {
    @Autowired
    lateinit var couponService: CouponService
    @Autowired
    lateinit var couponRepository: CouponRepository
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>
    @MockBean
    lateinit var rabbitService: RabbitService
    @Autowired
    lateinit var couponBoxRepository: CouponBoxRepository
    @Autowired
    lateinit var memberService: MemberService


    companion object {


        @JvmStatic
        var mysqlContainer: MySQLContainer<*> = MySQLContainer("mysql:latest").withExposedPorts(3306)
        @JvmStatic
        var redisContainer: GenericContainer<*> = GenericContainer("redis:latest").withExposedPorts(6379)

        @JvmStatic
        @BeforeAll //JUnit5에서 모든 테스트 메서드 실행 전에 실행될 메서드를 지정, 여기서는 테스트 실행 전에 MySQL 컨테이너를 시작하기 위해 사용
        fun beforeAll() {
            redisContainer.start()

        }
    }

}