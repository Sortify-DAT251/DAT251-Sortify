package backend.unit.controller

import backend.controller.UserController
import backend.manager.UserManager
import backend.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @MockBean
    private lateinit var userManager: UserManager


    @Test
    fun `should create a user successfully`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, username = "testuser", email = "test@example.com", password = "SecurePass123")
        val requestBody = objectMapper.writeValueAsString(mapOf("username" to "testuser", "email" to "test@example.com", "password" to "SecurePass123"))

        whenever(userManager.createUser(any(), any(), any())).thenReturn(user)

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `should return 400 Bad Request for invalid user creation`() {
        val requestBody = objectMapper.writeValueAsString(mapOf("email" to "invalid-email", "password" to "123"))

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should retrieve user by ID`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, username = "retrievedUser", email = "test@example.com", password = "SecurePass123")

        `when`(userManager.getUserById(userId)).thenReturn(user)

        mockMvc.perform(get("/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("retrievedUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `should return 404 Not Found when user does not exist`() {
        val userId = UUID.randomUUID()

        `when`(userManager.getUserById(userId)).thenReturn(null)

        mockMvc.perform(get("/users/$userId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should update user successfully`() {
        val userId = UUID.randomUUID()
        val updatedUser = User(id = userId, username = "updatedUser", email = "updated@example.com", password = "NewSecurePass123")
        val requestBody = objectMapper.writeValueAsString(mapOf("username" to "updatedUser", "email" to "updated@example.com", "password" to "NewSecurePass123"))

        whenever(userManager.updateUser(eq(userId), any<User>())).thenReturn(updatedUser)

        mockMvc.perform(put("/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("updatedUser"))
            .andExpect(jsonPath("$.email").value("updated@example.com"))
    }

    @Test
    fun `should return 404 when updating non-existent user`() {
        val userId = UUID.randomUUID()
        val requestBody = objectMapper.writeValueAsString(mapOf("username" to "updatedUser", "email" to "updated@example.com", "password" to "NewSecurePass123"))

        whenever(userManager.updateUser(eq(userId), any<User>())).thenReturn(null)

        mockMvc.perform(put("/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete user successfully`() {
        val userId = UUID.randomUUID()

        doNothing().`when`(userManager).deleteUser(userId)
        mockMvc.perform(delete("/users/$userId"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when deleting non-existent user`() {
        val userId = UUID.randomUUID()

        doThrow(NoSuchElementException("User not found")).`when`(userManager).deleteUser(userId)

        mockMvc.perform(delete("/users/$userId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Should return 200-OK when adding friend`() {
        val userId = UUID.randomUUID()
        val friendId = UUID.randomUUID()

        val requestBody = objectMapper.writeValueAsString(mapOf("friendId" to friendId.toString()))

        doNothing().`when`(userManager).addFriend(userId, friendId)

        mockMvc.perform(post("/users/$userId/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk)
    }


    @Test
    fun `Should return 200-OK when removing friend`() {
        val userId = UUID.randomUUID()
        val friendId = UUID.randomUUID()

        val requestBody = objectMapper.writeValueAsString(mapOf("friendId" to friendId.toString()))

        doNothing().`when`(userManager).removeFriend(userId, friendId)

        mockMvc.perform(delete("/users/$userId/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk)
    }
}
