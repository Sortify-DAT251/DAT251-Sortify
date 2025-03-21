package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "waste")
data class Waste (

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    @field:NotBlank
    val type: String,

    @field:NotBlank
    val info: String,

//    @OneToMany
//    val locations: MutableList<Location> = mutableListOf()
)