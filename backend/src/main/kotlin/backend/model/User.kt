package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "users")
data class User (

    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    @field:Email
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    @Column(nullable = false)
    @field:Size(min = 8)
    val password: String
)