package backend.manager

import backend.model.Locations
import backend.repository.LocationsRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.NoSuchElementException

@Service
class LocationsManager (private val LocationsRepository: LocationsRepository) {


    fun createLocations(locationname: String, address: String, latitude: Double, longtitude:Double): User {

        val locations = Locations(locationname = locationname, address = address, latitude = latitude, longtitude = longtitude)
        return LocationsRepository.save(locations)
    }

    fun getLocationsById(id: UUID): Locations? {
        return LocationsRepository.findById(id).orElse(null)
    }

    fun updateLocations(id: UUID, updatedLocations: Locations) : Locations {
        val existingLocations = LocationsRepository.findById(id).orElseThrow { NoSuchElementException("Location not found") }

        val LocationsToUpdate = existingLocations.copy(locationname = locationname, address = address, latitude = latitude, longtitude = longtitude)

        return LocationsRepository.save(LocationsToUpdate)
    }

    fun deleteLocations(id: UUID) {
        if (!LocationsRepository.existsById(id)) {
            throw NoSuchElementException("Location not found")
        }

        val locations = LocationsRepository.findById(id).orElseThrow { NoSuchElementException("Location with ID: $id not found") }

        }

        LocationsRepository.deleteById(id)
    }

    fun getAllLocations(): List<Locations> {
        return LocationsRepository.findAll()
    }
}