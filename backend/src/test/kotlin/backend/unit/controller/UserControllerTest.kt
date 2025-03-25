package backend.unit.controller

import backend.config.TestSecurityConfig
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
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class)
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

        mockMvc.perform(post("/api/users")
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

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should retrieve user by ID`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, username = "retrievedUser", email = "test@example.com", password = "SecurePass123")

        `when`(userManager.getUserById(userId)).thenReturn(user)

        mockMvc.perform(get("/api/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("retrievedUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `should return 404 Not Found when user does not exist`() {
        val userId = UUID.randomUUID()

        `when`(userManager.getUserById(userId)).thenReturn(null)

        mockMvc.perform(get("/api/users/$userId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should update user successfully`() {
        val userId = UUID.randomUUID()
        val updatedUser = User(id = userId, username = "updatedUser", email = "updated@example.com", password = "NewSecurePass123")
        val requestBody = objectMapper.writeValueAsString(mapOf("username" to "updatedUser", "email" to "updated@example.com", "password" to "NewSecurePass123"))

        whenever(userManager.updateUser(eq(userId), any<User>())).thenReturn(updatedUser)

        mockMvc.perform(put("/api/users/$userId")
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

        mockMvc.perform(put("/api/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete user successfully`() {
        val userId = UUID.randomUUID()

        doNothing().`when`(userManager).deleteUser(userId)
        mockMvc.perform(delete("/api/users/$userId"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when deleting non-existent user`() {
        val userId = UUID.randomUUID()

        doThrow(NoSuchElementException("User not found")).`when`(userManager).deleteUser(userId)

        mockMvc.perform(delete("/api/users/$userId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllUsers should return HTTP 200 with users`() {
        val user1 = User(
            id = UUID.randomUUID(),
            username = "alice123",
            email = "alice@example.com",
            password = "hashedpassword1"
        )

        val user2 = User(
            id = UUID.randomUUID(),
            username = "bob456",
            email = "bob@example.com",
            password = "hashedpassword2"
        )

        `when`(userManager.getAllUsers()).thenReturn(listOf(user1, user2))

        mockMvc.get("/api/users")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].id") { value(user1.id.toString()) }
                jsonPath("$[0].username") { value("alice123") }
                jsonPath("$[0].email") { value("alice@example.com") }
                jsonPath("$[1].id") { value(user2.id.toString()) }
                jsonPath("$[1].username") { value("bob456") }
                jsonPath("$[1].email") { value("bob@example.com") }
            }
    }

    @Test
    fun `Should return 200-OK when adding friend`() {
        val userId = UUID.randomUUID()
        val friendId = UUID.randomUUID()

        val requestBody = objectMapper.writeValueAsString(mapOf("friendId" to friendId.toString()))

        doNothing().`when`(userManager).addFriend(userId, friendId)

        mockMvc.perform(post("/api/users/$userId/friends")
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

        mockMvc.perform(delete("/api/users/$userId/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk)
    }

    @Test
    fun `Should login user successfully`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, username = "loginUser", email = "login@example.com", password = "SomeHashedPassword")
        val requestBody = objectMapper.writeValueAsString(
            mapOf("identifier" to "loginUser", "password" to "SomeHashedPassword")
        )

        whenever(userManager.loginUser(eq("loginUser"), eq("SomeHashedPassword"))).thenReturn(user)

        mockMvc.post("/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(userId.toString()) }
            jsonPath("$.username") { value("loginUser") }
            jsonPath("$.email") { value("login@example.com") }
        }
    }

    @Test
    fun `Should login user failure`() {
        val requestBody = objectMapper.writeValueAsString(mapOf("identifier" to "loginUser", "password" to "WrongPassword"))
        val errorMessage = "Invalid credentials"

        whenever(userManager.loginUser(eq("loginUser"), eq("WrongPassword"))).thenThrow(RuntimeException(errorMessage))

        mockMvc.post("/users/login") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.error") { value(errorMessage) }
        }
    }
}
