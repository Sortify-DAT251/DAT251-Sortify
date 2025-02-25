package backend.unit.controller

import backend.controller.UserController
import backend.manager.UserManager
import backend.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var userManager: UserManager
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        userManager = mock(UserManager::class.java)
    }

    @Test
    fun `should create a user successfully`() {
        val userId = UUID.randomUUID()
        val user = User(id = userId, email = "test@example.com", password = "SecurePass123")
        val requestBody = objectMapper.writeValueAsString(mapOf("email" to "test@example.com", "password" to "SecurePass123"))

        whenever(userManager.createUser(any(), any())).thenReturn(user)

        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(userId.toString()))
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
        val user = User(id = userId, email = "test@example.com", password = "SecurePass123")

        `when`(userManager.getUserById(userId)).thenReturn(user)

        mockMvc.perform(get("/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
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
        val updatedUser = User(id = userId, email = "updated@example.com", password = "NewSecurePass123")
        val requestBody = objectMapper.writeValueAsString(mapOf("email" to "updated@example.com", "password" to "NewSecurePass123"))

        `when`(userManager.updateUser(eq(userId), any())).thenReturn(updatedUser)

        mockMvc.perform(put("/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("updated@example.com"))
    }

    @Test
    fun `should return 404 when updating non-existent user`() {
        val userId = UUID.randomUUID()
        val requestBody = objectMapper.writeValueAsString(mapOf("email" to "updated@example.com", "password" to "NewSecurePass123"))

        `when`(userManager.updateUser(eq(userId), any())).thenReturn(null)

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
}
