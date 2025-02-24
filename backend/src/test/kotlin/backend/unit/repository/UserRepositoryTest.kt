package backend.unit.repository

import backend.model.User
import backend.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest // Uses an in-memory database for testing
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository // Injects the repository for testing

    @Test
    fun `save should persist user and generate ID`() {
        // Arrange
        val user = User(email = "test@example.com", password = "securepassword")

        // Act
        val savedUser = userRepository.save(user)

        // Assert
        assertNotNull(savedUser.id) // ID should be generated automatically
        assertEquals(user.email, savedUser.email)
        assertEquals(user.password, savedUser.password)
    }

    @Test
    fun `findById should return user if exists`() {
        // Arrange
        val user = userRepository.save(User(email = "test@example.com", password = "securepassword"))

        // Act
        val foundUser = userRepository.findById(user.id!!)

        // Assert
        assertTrue(foundUser.isPresent)
        assertEquals(user.email, foundUser.get().email)
        assertEquals(user.password, foundUser.get().password)
    }

    @Test
    fun `findById should return empty if user does not exist`() {
        // Act
        val foundUser = userRepository.findById(999999L) // ID that is guaranteed not to exist

        // Assert
        assertTrue(foundUser.isEmpty)
    }

    @Test
    fun `deleteById should remove user`() {
        // Arrange
        val user = userRepository.save(User(email = "delete@example.com", password = "password123"))
        assertTrue(userRepository.findById(user.id!!).isPresent) // Checks that the user exists

        // Act
        userRepository.deleteById(user.id!!)

        // Assert
        assertFalse(userRepository.findById(user.id!!).isPresent) // Should be deleted
    }
}