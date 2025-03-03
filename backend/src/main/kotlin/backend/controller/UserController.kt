package backend.controller

import backend.model.User
import backend.manager.UserManager
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/users")
@Validated
class UserController(private val userManager: UserManager) {

    @PostMapping
    fun createUser(@RequestBody @Valid request: UserRequest): ResponseEntity<User> {
        return try {
            val user = userManager.createUser(request.username, request.email, request.password)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
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
            val updatedUser = User(username = request.username, email = request.email, password = request.password)
            val user = userManager.updateUser(id, updatedUser)
            if (user != null) {
                ResponseEntity.ok(user)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
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

    @PostMapping("/{id}/friends")
    fun addFriend(@PathVariable id: UUID, @RequestBody @Valid request: AddFriendRequest): ResponseEntity<User> {
        val friend = userManager.getUserByUsername()
        userManager.addFriend(id, friendId = )
    }

}


data class UserRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    @field:Pattern(regexp = "^[a-zA-Z0-9_]*$")
    val username: String,

    @field:Email
    val email: String,

    @field:Size(min = 8)
    val password: String
)

data class AddFriendRequest(
    @field:Email(message = "Ugyldig e-postadresse")
    val email: String,
    val friendId: UUID
)