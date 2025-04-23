package backend.manager

import backend.model.Location
import backend.repository.LocationRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.NoSuchElementException
import backend.repository.WasteRepository

@Service
class LocationManager (private val locationRepository: LocationRepository, private val wasteRepository: WasteRepository) {


    fun createLocation(name: String, address: String, latitude: Double, longitude: Double, wasteTypes: List<String>): Location {
        val wastes = wasteRepository.findAllByTypeIn(wasteTypes) // or by name/UUID
        val location = Location(name = name, address = address, latitude = latitude, longitude = longitude, wasteTypes = wastes.toSet())
        return locationRepository.save(location)
    }

    fun getLocationById(id: Long): Location? {
        return locationRepository.findById(id).orElse(null)
    }

    fun updateLocation(id: Long, name: String, address: String, latitude: Double, longitude: Double, wasteTypes: List<String>): Location {
        val existingLocation = locationRepository.findById(id).orElseThrow { NoSuchElementException("Location not found") }
        val wastes = wasteRepository.findAllByTypeIn(wasteTypes)
        val updated = existingLocation.copy(
                name = name,
                address = address,
                latitude = latitude,
                longitude = longitude,
                wasteTypes = wastes.toSet()
        )
        return locationRepository.save(updated)
    }

    fun deleteLocation(id: Long) {
        if (!locationRepository.existsById(id)) {
            throw NoSuchElementException("Location not found")
        }
        locationRepository.deleteById(id)

        }

    fun getAllLocation(): List<Location> {
        return locationRepository.findAll()
    }
}