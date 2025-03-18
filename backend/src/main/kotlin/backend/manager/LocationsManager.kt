package backend.manager

import backend.model.Locations
import backend.repository.LocationsRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.NoSuchElementException

@Service
class LocationsManager (private val LocationsRepository: LocationsRepository) {


    fun createLocations(locationname: String, address: String, latitude: Double, longitude: Double): Locations {

        val locations = Locations(locationname = locationname, address = address, latitude = latitude, longitude = longitude)
        return LocationsRepository.save(locations)
    }

    fun getLocationsById(id: Long): Locations? {
        return LocationsRepository.findById(id).orElse(null)
    }

    fun updateLocations(id: Long, updatedLocations: Locations): Locations {
        val existingLocations = LocationsRepository.findById(id).orElseThrow { NoSuchElementException("Location not found") }

        val LocationsToUpdate = existingLocations.copy(
                id = existingLocations.id,
                locationname = updatedLocations.locationname,
                address = updatedLocations.address,
                latitude = updatedLocations.latitude,
                longitude = updatedLocations.longitude
        )

        return LocationsRepository.save(LocationsToUpdate)
    }

    fun deleteLocations(id: Long) {
        if (!LocationsRepository.existsById(id)) {
            throw NoSuchElementException("Location not found")
        }
        LocationsRepository.deleteById(id)

        }

    fun getAllLocations(): List<Locations> {
        return LocationsRepository.findAll()
    }
}