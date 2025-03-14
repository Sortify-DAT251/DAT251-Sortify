package backend.model

import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.util.UUID



@Entity
@Table(name = "locations")
data class Locations(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        @field: NotBlank
        val locationname: String,

        @Column(nullable = false, unique = true)
        @field: NotBlank
        val address: String,

        @Column(nullable = false)
        @field: NotNull
        @field: Min(-90) @field: Max(90)
        val latitude: Double,

        @Column(nullable = false)
        @field: NotNull
        @field: Min(-180) @field: Max(180)
        val longitude: Double


)