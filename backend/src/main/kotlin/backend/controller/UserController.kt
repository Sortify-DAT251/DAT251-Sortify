package backend.controller

import backend.model.User
import backend.manager.UserManager
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
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
    fun createUser(@RequestBody @Valid request: CreateUserRequest): ResponseEntity<User> {
        return try {
            val user = userManager.createUser(request.email, request.password)
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
    fun updateUser(@PathVariable id: UUID, @RequestBody @Valid request: UpdateUserRequest): ResponseEntity<User> {
        return try {
            val updatedUser = User(email = request.email, password = request.password)
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

}

// Dto-klasser for å motta og validere request-data
data class CreateUserRequest(
    @field:Email(message = "Ugyldig e-postadresse")
    val email: String,
    @field:Size(min = 8, message = "Passordet må være minst 8 tegn")
    val password: String
)

data class UpdateUserRequest(
    @field:Email(message = "Ugyldig e-postadresse")
    val email: String,
    @field:Size(min = 8, message = "Passordet må være minst 8 tegn")
    val password: String
)