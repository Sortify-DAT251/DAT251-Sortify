package backend.unit.model

import backend.model.User
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserTest {

    private  var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `should generate an ID automatically as UUID`() {
        val user = User(id = UUID.randomUUID(), email = "test@example.com", password = "SecurePass123")
        assertNotNull(user.id, "User ID should not be null")
        assertTrue(user.id is UUID, "User ID should be of type UUID")
    }

    @Test
    fun `should accept a valid email`() {
        val user = User(id = UUID.randomUUID(), email = "valid@example.com", password = "SecurePass123")
        val violations = validator.validate(user)

        assertTrue(violations.isEmpty(), "User with valid email should pass validation")
    }

    @Test
    fun `should reject an invalid email`() {
        val user = User(id = UUID.randomUUID(), email = "invalid-email", password = "SecurePass123")
        val violations = validator.validate(user)

        assertFalse(violations.isEmpty(), "User with invalid email should fail validation")
        assertTrue(violations.any { it.message.contains("must be a well-formed email address") })
    }

    @Test
    fun `should reject password shorter than 8 characters`() {
        val user = User(id = UUID.randomUUID(), email = "test@example.com", password = "1234567")
        val violations = validator.validate(user)

        assertFalse(violations.isEmpty(), "User with short password should fail validation")
        assertTrue(violations.any { it.message.contains("size must be between 8 and") })
    }

    @Test
    fun `should accept valid password`() {
        val user = User(id = UUID.randomUUID(), email = "test@example.com", password = "SecurePass123")
        val violations = validator.validate(user)

        assertTrue(violations.isEmpty(), "User with valid password should pass validation")
    }
}