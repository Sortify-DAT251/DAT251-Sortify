package backend

import backend.config.PostGresTestConfig
import backend.repository.UserRepository
import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(PostGresTestConfig::class)  // Import the custom PostgreSQL test config
class BackendApplicationTests {

	@Autowired
	private lateinit var userRepository: UserRepository

	@Test
	fun contextLoads() {
		assertNotNull(userRepository)
	}
}


