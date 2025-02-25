package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "users")
data class User (
    //@Id

    //@GeneratedValue(strategy = GenerationType.UUID)
    //val id: UUID? = null,
    //@GeneratedValue
    //val id: UUID = UUID.randomUUID(),

    //var email: String,
    //var password: String

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    @Column(nullable = false)
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String


)