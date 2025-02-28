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
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserManagerTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userManager: UserManager

    @Test
    fun `createUser should generate an ID and save user`(){
        val email = "test@example.com"
        val password = "example123"

        `when`(userRepository.save(any())).thenAnswer { invocation -> invocation.arguments[0] as User }

        val createdUser = userManager.createUser(email, password)
        val userCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userCaptor.capture())
        val savedUser = userCaptor.value

        assertEquals(email, savedUser.email)
        assertEquals(password, savedUser.password)
        assertNull(savedUser.id, "ID skal ikke være generert før lagring")
        assertEquals(savedUser, createdUser)
    }

    @Test
    fun `createUser should throw exception if save fails`() {
        val email = "error@example.com"
        val password = "password123"
        `when`(userRepository.save(any())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<RuntimeException> {
            userManager.createUser(email, password)
        }
        assertEquals("Database error", exception.message)
    }

    @Test
    fun `getUserById should return user if found`() {
        val userId = UUID.randomUUID()
        val expectedUser = User(id = userId, email = "found@example.com", password = "password")
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser))

        val result = userManager.getUserById(userId)

        assertNotNull(result)
        assertEquals(expectedUser, result)
        verify(userRepository).findById(userId)
    }

    @Test
    fun `getUserById should return null if user not found`() {
        val userId = UUID.randomUUID()
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        val result = userManager.getUserById(userId)

        assertNull(result)
        verify(userRepository).findById(userId)
    }

    @Test
    fun `updateUser should update existing user and return updated user`() {
        val userId = UUID.randomUUID()
        val existingUser = User(id = userId, email = "old@example.com", password = "oldPassword")
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(existingUser))
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = userManager.updateUser(userId, updatedUser)

        assertEquals(updatedUser.email, result.email)
        assertEquals(updatedUser.password, result.password)
        assertEquals(userId, result.id)
        verify(userRepository).findById(userId)
        verify(userRepository).save(updatedUser)
    }

    @Test
    fun `updateUser should throw exception if user does not exist`() {
        val userId = UUID.randomUUID()
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            userManager.updateUser(userId, updatedUser)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findById(userId)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `deleteUser should remove user if found`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, email = "user@example.com", password = "Password123")
        `when`(userRepository.existsById(userId)).thenReturn(true)
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        userManager.deleteUser(userId)

        verify(userRepository).existsById(userId)
        verify(userRepository).deleteById(userId)
    }

    @Test
    fun `deleteUser should throw exception if user does not exist`() {
        val userId = UUID.randomUUID()
        `when`(userRepository.existsById(userId)).thenReturn(false)

        val exception = assertThrows<NoSuchElementException> {
            userManager.deleteUser(userId)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).existsById(userId)
        verify(userRepository, never()).deleteById(any())
    }

    // Mock function for testing friendsLists
    private fun mockUserandFriend(): Pair<User, User> {
        val userId = UUID.randomUUID()
        val friendId = UUID.randomUUID()
        val user = User(id = userId, email = "user@example.com", password = "Password123")
        val friend = User(id = friendId, email = "friend@example.com", password = "Password123")

        // Mock repository behavior
        `when`(userRepository.existsById(userId)).thenReturn(true)
        `when`(userRepository.existsById(friendId)).thenReturn(true)
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(userRepository.findById(friendId)).thenReturn(Optional.of(friend))
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] }

        return Pair(user, friend)
    }

    @Test
    fun `addFriend should add target to users friendsList and add user to targets friendsList`() {

        val (user, friend) = mockUserandFriend()
        val userId = user.id!!
        val friendId = friend.id!!

        userManager.addFriend(userId, friendId)

        assert(user.friends.contains(friend))
        assert(friend.friends.contains(user))

        // Verify repository interactions
        verify(userRepository).existsById(userId)
        verify(userRepository).existsById(friendId)
        verify(userRepository).findById(userId)
        verify(userRepository).findById(friendId)
        verify(userRepository, times(2)).save(any<User>())
    }

    @Test
    fun `removeFriend should remove target from users friendsList and remove user from targets friendsList`() {
        val (user, friend) = mockUserandFriend()
        val userId = user.id!!
        val friendId = friend.id!!

        userManager.addFriend(userId, friendId)

        assert(user.friends.contains(friend))
        assert(friend.friends.contains(user))

        userManager.removeFriend(userId, friendId)

        assert(!user.friends.contains(friend))
        assert(!friend.friends.contains(user))

        // Verify repository interactions
        verify(userRepository, times(2)).findById(userId)
        verify(userRepository, times(2)).findById(friendId)
        verify(userRepository, times(4)).save(any<User>())
    }

    @Test
    fun `deleteUser should remove all friends from users friendsList and remove user from all friends friendsList`() {
        val (user, friend) = mockUserandFriend()
        val userId = user.id!!
        val friendId = friend.id!!

        userManager.addFriend(userId, friendId)

        assert(user.friends.contains(friend))
        assert(friend.friends.contains(user))

        // Delete friend-account
        userManager.deleteUser(friendId)

        assert(!user.friends.contains(friend))
        assert(user.friends.isEmpty())
    }
}