package backend.controller

import backend.model.Location
import backend.manager.LocationManager
import backend.service.LocationService
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
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api/locations")
@Validated
class LocationsController(private val LocationManager: LocationManager,
                          private val locationService: LocationService) {

    @PostMapping
    fun createLocations(@RequestBody @Valid request: LocationsRequest): ResponseEntity<Any> {
        return try {
            val location = LocationManager.createLocation(request.name, request.address, request.latitude, request.longitude, request.wasteTypes)
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
            val location = LocationManager.updateLocation(
                    id,
                    request.name,
                    request.address,
                    request.latitude,
                    request.longitude,
                    request.wasteTypes
            )
            ResponseEntity.ok(location)
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

    @GetMapping("/sorted")
    fun getAllLocationsSorted(
        @RequestParam lat: Double,
        @RequestParam lon: Double
    ): ResponseEntity<List<LocationDto>> {
        val locationsWithDistance = locationService.getDistanceFromUser(lat, lon)
        val locationDtos = locationsWithDistance.map { (location, _) -> LocationDto.fromEntity(location) }
        return ResponseEntity.ok(locationDtos)
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

        @field:Size(min = 1)
        val wasteTypes: List<String>

)

// LocationDto for returning location data to the client
data class LocationDto(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val wasteTypes: List<String>
) {
    companion object {
        fun fromEntity(location: Location): LocationDto {
            return LocationDto(
                id = location.id!!,
                name = location.name,
                address = location.address,
                latitude = location.latitude,
                longitude = location.longitude,
                    wasteTypes = location.wasteTypes.map { it.type }
            )
        }
    }
}