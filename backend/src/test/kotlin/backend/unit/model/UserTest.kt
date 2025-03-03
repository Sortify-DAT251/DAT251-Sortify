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
        val user = User(id = UUID.randomUUID(), username = "User1", email = "test@example.com", password = "SecurePass123")
        assertNotNull(user.id, "User ID should not be null")
        assertTrue(user.id is UUID, "User ID should be of type UUID")
    }

    @Test
    fun `should accept a valid email`() {
        val user = User(id = UUID.randomUUID(), username = "ValidUser", email = "valid@example.com", password = "SecurePass123")
        val violations = validator.validate(user)

        assertTrue(violations.isEmpty(), "User with valid email should pass validation")
    }

    @Test
    fun `should accept a valid username`() {
        val user = User(id = UUID.randomUUID(), username = "ValidUser_123", email = "test@example.com", password = "SecurePass123")
        val violations = validator.validate(user)
        assertTrue(violations.isEmpty(), "User with a valid username should pass validation")
    }

    @Test
    fun `should reject invalid usernames`() {
        val invalidUsernames = listOf("", "ab", "abcdefghijklmnopqrstu", "Invalid Username", "Invalid!")
        invalidUsernames.forEach { username ->
            val user = User(id = UUID.randomUUID(), username = username, email = "test@example.com", password = "SecurePass123")
            val violations = validator.validate(user)
            assertFalse(violations.isEmpty(), "Username '$username' should be invalid and fail validation")
        }
    }

    @Test
    fun `should reject an invalid email`() {
        val user = User(id = UUID.randomUUID(), username = "InvalidUser", email = "invalid-email", password = "SecurePass123")
        val violations = validator.validate(user)

        assertFalse(violations.isEmpty(), "User with invalid email should fail validation")
        assertTrue(violations.any { it.message.contains("must be a well-formed email address") })
    }

    @Test
    fun `should reject password shorter than 8 characters`() {
        val user = User(id = UUID.randomUUID(), username = "ShortPassUser", email = "test@example.com", password = "1234567")
        val violations = validator.validate(user)

        assertFalse(violations.isEmpty(), "User with short password should fail validation")
        assertTrue(violations.any { it.message.contains("size must be between 8 and") })
    }

    @Test
    fun `should accept valid password`() {
        val user = User(id = UUID.randomUUID(), username = "SecurePass", email = "test@example.com", password = "SecurePass123")
        val violations = validator.validate(user)

        assertTrue(violations.isEmpty(), "User with valid password should pass validation")
    }

    @Test
    fun `should be able to find users in the friends list`() {
        val user1 = User(username = "user1", id = UUID.randomUUID(), email = "test@example.com", password = "SomethingSomething")
        val user2 = User(username = "user2", id = UUID.randomUUID(), email = "test@example.com", password = "SomethingSomething2")
        val user3 = User(username = "user3", id = UUID.randomUUID(), email = "test@example.com", password = "SomethingSomething3")

        assertTrue(user1.friends.isEmpty())

        // Add friends to list.
        user1.friends.add(user2);
        user1.friends.add(user3);

        // The list is not empty.
        assertFalse(user1.friends.isEmpty())

        // Contains both user2 and user3.
        assertTrue(user1.friends.contains(user2))
        assertTrue(user1.friends.contains(user3))

        // First user in list is user2.
        assertTrue(user1.friends.get(0) == user2)

        // List is still not empty and contains user3, when user3 is removed.
        user1.friends.remove(user2)
        assertFalse(user1.friends.isEmpty())
        assertTrue(user1.friends.contains(user3))

        // Empty the list, and check that it is empty
        user1.friends.clear()
        assertTrue(user1.friends.isEmpty())


    }
}