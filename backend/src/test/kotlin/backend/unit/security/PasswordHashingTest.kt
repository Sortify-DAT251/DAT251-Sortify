package backend.unit.security

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PasswordHashingTest {

    private val encoder = BCryptPasswordEncoder()

    @Test
    fun `should hash password correctly`() {
        val rawPassword = "somePassword"
        val hashedPassword = encoder.encode(rawPassword)

        assertNotEquals(rawPassword, hashedPassword)
    }

    @Test
    fun `should match hashed password`() {
        val rawPassword = "somePassword"
        val hashedPassword = encoder.encode(rawPassword)

        assertTrue(encoder.matches(rawPassword, hashedPassword))
    }

    @Test
    fun `should fail for incorrect password`() {
        val rawPassword = "somePassword"
        val wrongPassword = "wrongPassword"
        val hashedPassword = encoder.encode(rawPassword)

        assertFalse(encoder.matches(wrongPassword, hashedPassword))
    }

}