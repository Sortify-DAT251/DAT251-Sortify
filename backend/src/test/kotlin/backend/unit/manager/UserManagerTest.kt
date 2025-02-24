package backend.unit.manager

import backend.manager.UserManager
import backend.repository.UserRepository
import backend.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserManagerTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userManager: UserManager

    @Test
    fun `createUser should generate an ID and save user`(){
        // Arrange
        val email = "test@example.com"
        val password = "example123"

        // Returns the same User object that is passed in, so we can test the ID in the return value
        `when`(userRepository.save(any())).thenAnswer { invocation -> invocation.arguments[0] as User }

        // Act
        val createdUser = userManager.createUser(email, password)

        // Assert
        // Captures the User object actually sent to repository.save()
        val userCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userCaptor.capture())
        val savedUser = userCaptor.value

        // Checks that email, password, and ID are correctly set
        assertEquals(email, savedUser.email)
        assertEquals(password, savedUser.password)
        assertNotNull(savedUser.id, "ID skal være generert før lagring")

        // Verifies that the return value from createUser is the same object
        assertEquals(savedUser, createdUser)
    }

    @Test
    fun `createUser should throw exception if save fails`() {
        // Arrange
        val email = "error@example.com"
        val password = "password123"
        `when`(userRepository.save(any())).thenThrow(RuntimeException("Database error"))

        // Act & Assert
        val exception = assertThrows<RuntimeException> {
            userManager.createUser(email, password)
        }
        assertEquals("Database error", exception.message)
    }

    @Test
    fun `getUserById should return user if found`() {
        // Arrange
        val userId = 1L
        val expectedUser = User(id = userId, email = "found@example.com", password = "password")
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser))

        // Act
        val result = userManager.getUserById(userId)

        // Assert
        assertNotNull(result)
        assertEquals(expectedUser, result)
        verify(userRepository).findById(userId)
    }

    @Test
    fun `getUserById should return null if user not found`() {
        // Arrange
        val userId = 2L
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        // Act
        val result = userManager.getUserById(userId)

        // Assert
        assertNull(result)
        verify(userRepository).findById(userId)
    }

    @Test
    fun `updateUser should update existing user and return updated user`() {
        // Arrange
        val userId = 1L
        val existingUser = User(id = userId, email = "old@example.com", password = "oldPassword")
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(existingUser))
        // Returns the user that is passed in
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val result = userManager.updateUser(userId, updatedUser)

        // Assert
        assertEquals(updatedUser.email, result.email)
        assertEquals(updatedUser.password, result.password)
        assertEquals(userId, result.id)
        verify(userRepository).findById(userId)
        verify(userRepository).save(updatedUser)
    }

    @Test
    fun `updateUser should throw exception if user does not exist`() {
        // Arrange
        val userId = 2L
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        // Act & Assert
        val exception = assertThrows<NoSuchElementException> {
            userManager.updateUser(userId, updatedUser)
        }
        assertEquals("User not found", exception.message)
        verify(userRepository).findById(userId)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `deleteUser should remove user if found`() {
        // Arrange
        val userId = 1L
        `when`(userRepository.existsById(userId)).thenReturn(true)

        // Act
        userManager.deleteUser(userId)

        // Assert
        verify(userRepository).existsById(userId)
        verify(userRepository).deleteById(userId)
    }

    @Test
    fun `deleteUser should throw exception if user does not exist`() {
        // Arrange
        val userId = 2L
        `when`(userRepository.existsById(userId)).thenReturn(false)

        // Act
        val exception = assertThrows<NoSuchElementException> {
            userManager.deleteUser(userId)
        }
        assertEquals("User not found", exception.message)
        verify(userRepository).existsById(userId)
        verify(userRepository, never()).deleteById(any())
    }
}