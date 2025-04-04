package backend.controller

import backend.model.Location
import backend.manager.LocationManager
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.UUID
// @CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api/locations")
@Validated
class LocationsController(private val LocationManager: LocationManager) {

    @PostMapping
    fun createLocations(@RequestBody @Valid request: LocationsRequest): ResponseEntity<Any> {
        return try {
            val location = LocationManager.createLocation(request.name, request.address, request.latitude, request.longitude, request.info)
            ResponseEntity.status(HttpStatus.CREATED).body(location)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Location already exists"))
        }
    }

    @GetMapping("/{id}")
    fun getLocations(@PathVariable id: Long): ResponseEntity<Location> {
        val location = LocationManager.getLocationById(id)
        return if ( location != null) ResponseEntity.ok(location)
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}")
    fun updateLocations(@PathVariable id: Long, @RequestBody @Valid request: LocationsRequest): ResponseEntity<Location> {
        return try {
            val updatedLocation = Location(
                    name = request.name,
                    address = request.address,
                    latitude = request.latitude,
                    longitude = request.longitude,
                    info = request.info
            )
            val location = LocationManager.updateLocation(id, updatedLocation)

            if (location != null) {
                ResponseEntity.ok(location)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteLocations(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            LocationManager.deleteLocation(id)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    fun getAllLocations(): ResponseEntity<List<Location>> {
        val locations = LocationManager.getAllLocation()
        return ResponseEntity.ok(locations)
    }


}


data class LocationsRequest(
        @field:NotBlank
        @field:Size(min = 3, max = 30)
        @field:Pattern(regexp = "^[a-zA-Z0-9_]*$")
        val name: String,

        @field:NotBlank
        val address: String,

        @field:Size(min = 1)
        val latitude: Double,

        @field:Size(min = 1)
        val longitude: Double,

        @field:NotBlank
        val info: String

)