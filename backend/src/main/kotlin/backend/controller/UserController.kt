package backend.controller

import backend.model.User
import backend.manager.UserManager
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(private val userManager: UserManager) {

    @PostMapping
    fun createUser(@RequestBody @Valid request: UserRequest): ResponseEntity<Any> {
        return try {
            val user = userManager.createUser(
                request.username,
                request.email,
                request.password,
                request.firstName,
                request.lastName)

            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Username or email is already in use."))
        }
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): ResponseEntity<User> {
        val user = userManager.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody @Valid request: UserRequest): ResponseEntity<User> {
        return try {
            val updatedUser = User(
                username = request.username,
                email = request.email,
                password = request.password,
                firstName = request.firstName,
                lastName = request.lastName)

            val user = userManager.updateUser(id, updatedUser)
            ResponseEntity.ok(user)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        return try {
            userManager.deleteUser(id)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userManager.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @PostMapping("{id}/friends")
    fun addFriend(@PathVariable id: UUID, @RequestBody @Valid request: FriendRequest): ResponseEntity<Void> {
        userManager.addFriend(id, request.friendId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("{id}/friends")
    fun removeFriend(@PathVariable id: UUID, @RequestBody @Valid request: FriendRequest): ResponseEntity<Void> {
        userManager.removeFriend(id, request.friendId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody @Valid request: LoginRequest): ResponseEntity<Any> {
        return try {
            val user = userManager.loginUser(request.identifier, request.password)
            ResponseEntity.ok(user)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to ex.message))
        }
    }
}

data class UserRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 30)
    @field:Pattern(regexp = "^[a-zA-Z0-9_]*$")
    val username: String,

    @field:Email
    val email: String,

    @field:Size(min = 8)
    val password: String,

    val firstName: String? = null,
    val lastName: String? = null
)

// Data class for adding and removing friends
data class FriendRequest(
    val friendId: UUID
)

data class LoginRequest(
    @field:NotBlank
    val identifier: String,

    @field:NotBlank
    @field:Size(min = 8)
    val password: String
)