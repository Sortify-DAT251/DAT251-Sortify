package backend.unit.repository

import backend.config.PostGresTestConfig
import backend.model.User
import backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest
@Import(PostGresTestConfig::class)  // Import PostgreSQL TestContainer Config
@ExtendWith(SpringExtension::class)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    @Transactional
    fun `save should persist user and generate ID`() {
        val user = User(email = "test@example.com", password = "securepassword")
        val savedUser = userRepository.save(user)

        assertNotNull(savedUser.id)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.password, savedUser.password)
    }

    @Test
    @Transactional
    fun `findById should return user if exists`() {
        val user = userRepository.save(User(email = "test@example.com", password = "securepassword"))
        val foundUser = userRepository.findById(user.id!!)

        assertTrue(foundUser.isPresent)
        assertEquals(user.email, foundUser.get().email)
        assertEquals(user.password, foundUser.get().password)
    }

    @Test
    @Transactional
    fun `findById should return empty if user does not exist`() {
        val nonExistentId = UUID.randomUUID()
        val foundUser = userRepository.findById(nonExistentId)

        assertTrue(foundUser.isEmpty)
    }

    @Test
    @Transactional
    fun `deleteById should remove user`() {
        val user = userRepository.save(User(email = "delete@example.com", password = "password123"))
        assertTrue(userRepository.findById(user.id!!).isPresent) // Checks that the user exists

        userRepository.deleteById(user.id!!)

        assertFalse(userRepository.findById(user.id!!).isPresent) // Should be deleted
    }
}