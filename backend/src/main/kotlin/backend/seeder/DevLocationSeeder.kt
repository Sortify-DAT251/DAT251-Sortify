package backend.seeder

import backend.model.Location
import backend.repository.LocationRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class DevLocationSeeder(
        private val locationRepository: LocationRepository,
        private val objectMapper: ObjectMapper
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println(">>> Seeder is running")

        try {
            val inputStream = javaClass.getResourceAsStream("/dev-data/locations.json")
            if (inputStream == null) {
                println("❌ locations.json not found!")
                return
            }

            val locations: List<Location> = objectMapper.readValue(inputStream, object : TypeReference<List<Location>>() {})
            println("✅ Read ${locations.size} locations")

            if (locationRepository.count() == 0L) {
                locationRepository.saveAll(locations)
                println("✅ Seeded locations")
            } else {
                println("ℹ️ Skipping seeding, already exists")
            }

        } catch (e: Exception) {
            println("❌ Exception during seeding: ${e.message}")
            e.printStackTrace()
        }
    }
}

