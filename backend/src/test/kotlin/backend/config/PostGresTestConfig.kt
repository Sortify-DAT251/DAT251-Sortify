package backend.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.time.Duration
import javax.sql.DataSource

@TestConfiguration
class PostGresTestConfig {

    companion object {
        private val postgres = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
            setWaitStrategy(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(2))) // Wait until ready

        }
    }

    @PostConstruct
    fun startContainer() {
        postgres.start()
    }

    @PreDestroy
    fun stopContainer() {
        postgres.stop()
    }

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.url = postgres.jdbcUrl
        dataSource.username = postgres.username
        dataSource.password = postgres.password
        return dataSource
    }
}