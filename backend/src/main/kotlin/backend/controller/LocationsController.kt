package backend.controller

import backend.model.Locations
import backend.manager.LocationsManager
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
@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/locations")
@Validated
class LocationsController(private val LocationsManager: LocationsManager) {

    @PostMapping
    fun createLocations(@RequestBody @Valid request: LocationsRequest): ResponseEntity<Any> {
        return try {
            val location = LocationsManager.createLocations(request.locationname, request.address, request.latitude, request.longitude, request.info)
            ResponseEntity.status(HttpStatus.CREATED).body(location)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Location already exists"))
        }
    }

    @GetMapping("/{id}")
    fun getLocations(@PathVariable id: Long): ResponseEntity<Locations> {
        val location = LocationsManager.getLocationsById(id)
        return if ( location != null) ResponseEntity.ok(location)
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}")
    fun updateLocations(@PathVariable id: Long, @RequestBody @Valid request: LocationsRequest): ResponseEntity<Locations> {
        return try {
            val updatedLocation = Locations(
                    locationname = request.locationname,
                    address = request.address,
                    latitude = request.latitude,
                    longitude = request.longitude,
                    info = request.info
            )
            val location = LocationsManager.updateLocations(id, updatedLocation)

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
            LocationsManager.deleteLocations(id)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    fun getAllLocations(): ResponseEntity<List<Locations>> {
        val locations = LocationsManager.getAllLocations()
        return ResponseEntity.ok(locations)
    }


}


data class LocationsRequest(
        @field:NotBlank
        @field:Size(min = 3, max = 30)
        @field:Pattern(regexp = "^[a-zA-Z0-9_]*$")
        val locationname: String,

        @field:NotBlank
        val address: String,

        @field:Size(min = 1)
        val latitude: Double,

        @field:Size(min = 1)
        val longitude: Double,

        @field:NotBlank
        val info: String

)