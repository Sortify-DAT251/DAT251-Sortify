package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.UUID
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "locations")
data class Location(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        @field: NotBlank
        val name: String,

        @Column(nullable = false, unique = true)
        @field: NotBlank
        val address: String,

        @Column(nullable = false)
        @field: NotNull
        @field: Min(-90) @field: Max(90)
        val latitude: Double,

        @Column(nullable = false)
        @field: Min(-180) @field: Max(180)
        @field: NotNull
        val longitude: Double,

        @Column(nullable = false)
        @field: NotBlank
        val info: String
)