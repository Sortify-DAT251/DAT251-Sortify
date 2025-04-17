package backend.service

import backend.model.Location
import backend.repository.LocationRepository
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class LocationService(private val locationRepository: LocationRepository) {

    fun getDistanceFromUser(userLat: Double, userLon: Double): List<Pair<Location, Double>> {
        val locations = locationRepository.findAll()

        return locations
            .mapNotNull { location ->
                val distance = haversine(userLat, userLon, location.latitude, location.longitude)
                location to distance
            }
            .sortedBy { it.second }
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2).pow(2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
}