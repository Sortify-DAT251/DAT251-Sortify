package backend.unit.service

import backend.model.Location
import backend.model.Waste
import backend.repository.LocationRepository
import backend.service.LocationService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class LocationServiceTest {

    @TestConfiguration
    class LocationServiceTestConfig {
        @Bean
        fun locationService(locationRepository: LocationRepository): LocationService {
            return LocationService(locationRepository)
        }
    }

    @Autowired
    private lateinit var locationService: LocationService

    @MockitoBean
    private lateinit var locationRepository: LocationRepository

    @Test
    fun `should calculate distance from user and return sorted locations`() {
        val userLat = 60.39
        val userLon = 5.32

        val dummyWaste = Waste(UUID.randomUUID(), "Test Waste", "Plast", "Dummy Info")
        val wasteSet = setOf(dummyWaste)

        val locations = listOf(
                Location(1, "Location A", "Address A", 60.3818, 5.2294, wasteSet),
                Location(2, "Location B", "Address B", 60.4047, 5.2103, wasteSet),
                Location(3, "Location C", "Address C", 60.3803, 5.3442, wasteSet)
        )

        whenever(locationRepository.findAll()).thenReturn(locations)

        val result = locationService.getDistanceFromUser(userLat, userLon)

        result.forEachIndexed { index, pair ->
            println("Location ${pair.first.name} - Distance: ${pair.second} km")
        }

        assertEquals(3, result.size)
        assertEquals("Location C", result[0].first.name)
        assertEquals("Location A", result[1].first.name)
        assertEquals("Location B", result[2].first.name)

        val distanceC = result[0].second
        assert(distanceC > 0.0)
    }
}
