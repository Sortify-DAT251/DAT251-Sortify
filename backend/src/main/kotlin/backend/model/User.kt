package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "users")
data class User (

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    @field:Pattern(regexp = "^[a-zA-Z0-9_]*+$")
    val username: String,

    @Column(nullable = false, unique = true)
    @field:Email
    @field:NotBlank
    val email: String,

    @Column(nullable = false)
    @field:Size(min = 8)
    val password: String
)