package backend.manager

import backend.model.Location
import backend.repository.LocationRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.NoSuchElementException

@Service
class LocationManager (private val LocationRepository: LocationRepository) {


    fun createLocation(name: String, address: String, latitude: Double, longitude: Double, info: String): Location {

        val Location = Location(name = name, address = address, latitude = latitude, longitude = longitude, info = info)
        return LocationRepository.save(Location)
    }

    fun getLocationById(id: Long): Location? {
        return LocationRepository.findById(id).orElse(null)
    }

    fun updateLocation(id: Long, updatedLocation: Location): Location {
        val existingLocation = LocationRepository.findById(id).orElseThrow { NoSuchElementException("Location not found") }

        val LocationToUpdate = existingLocation.copy(
                id = existingLocation.id,
                name = updatedLocation.name,
                address = updatedLocation.address,
                latitude = updatedLocation.latitude,
                longitude = updatedLocation.longitude,
                info = updatedLocation.info
        )

        return LocationRepository.save(LocationToUpdate)
    }

    fun deleteLocation(id: Long) {
        if (!LocationRepository.existsById(id)) {
            throw NoSuchElementException("Location not found")
        }
        LocationRepository.deleteById(id)

        }

    fun getAllLocation(): List<Location> {
        return LocationRepository.findAll()
    }
}