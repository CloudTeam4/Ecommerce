package com.teamsparta.ecommerce.integrationtest

import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MySQLContainer

@ActiveProfiles("test") //테스트용 프로파일인 "test"를 활성화, 이 프로파일을 사용하면 테스트용 환경 설정이 로드.
@SpringBootTest
@Sql("classpath:/db/init_table.sql") //테스트용 데이터베이스의 초기 데이터를 로드하는데 사용, classpath를 통해 클래스패스 상의 SQL 파일을 지정
@Sql("classpath:/db/dml.sql") //DML 쿼리를 실행하여 초기 데이터베이스 상태를 설정하는데 사용
abstract class IntegrationTest {

    companion object {

        @JvmStatic //static
        //테스트에서 사용할 MySQL 컨테이너를 선언, 이 컨테이너는 테스트 실행 전에 시작되고 테스트 종료 후에 중지
        //Docker 켰는지 꼭 확인!!
        var container: MySQLContainer<*> = MySQLContainer("mysql:8.0.33");

        @JvmStatic
        @BeforeAll //JUnit5에서 모든 테스트 메서드 실행 전에 실행될 메서드를 지정, 여기서는 테스트 실행 전에 MySQL 컨테이너를 시작하기 위해 사용
        fun beforeAll() {
        }
    }

}