package backend.seeder

import backend.model.Location
import backend.repository.LocationRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import backend.repository.WasteRepository

@Component
@Profile("dev")
class DevLocationSeeder(
        private val locationRepository: LocationRepository,
        private val wasteRepository: WasteRepository,
        private val objectMapper: ObjectMapper
) : ApplicationRunner {

    data class LocationSeedDTO(
            val name: String,
            val address: String,
            val latitude: Double,
            val longitude: Double,
            val wasteTypes: List<String>
    )


    override fun run(args: ApplicationArguments?) {
        println(">>> Location Seeder is running")

        try {
            val inputStream = javaClass.getResourceAsStream("/dev-data/locations.json")
            if (inputStream == null) {
                println("❌ locations.json not found!")
                return
            }

            val locationSeedDTOs: List<LocationSeedDTO> =
                    objectMapper.readValue(inputStream, object : TypeReference<List<LocationSeedDTO>>() {})

            println("✅ Read ${locationSeedDTOs.size} locations from file")

            if (locationRepository.count() == 0L) {
                val locations = locationSeedDTOs.map { dto ->
                    val wastes = wasteRepository.findAllByTypeIn(dto.wasteTypes)
                    Location(
                            name = dto.name,
                            address = dto.address,
                            latitude = dto.latitude,
                            longitude = dto.longitude,
                            wasteTypes = wastes.toSet()
                    )
                }
                locationRepository.saveAll(locations)
                println("✅ Seeded ${locations.size} locations")
            } else {
                println("ℹ️ Skipping seeding, locations already exist")
            }

        } catch (e: Exception) {
            println("❌ Exception during location seeding: ${e.message}")
            e.printStackTrace()
        }
    }
}


