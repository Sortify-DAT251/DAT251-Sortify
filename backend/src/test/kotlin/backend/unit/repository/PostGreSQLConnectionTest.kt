package backend.unit.repository

import backend.config.PostGresTestConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@Import(PostGresTestConfig::class)
class PostGreSQLConnectionTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `should successfully connect to PostGreSQL database`() {
        val result = jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
        assert(result == 1)
    }

    @Test
    fun `should check database name`() {
        val databaseName = jdbcTemplate.queryForObject("SELECT current_database()", String::class.java)
        assert(databaseName == "testdb")
    }
}