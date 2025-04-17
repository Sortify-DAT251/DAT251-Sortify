package backend.seeder

import backend.model.Waste
import backend.repository.WasteRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class DevWasteSeeder(
        private val wasteRepository: WasteRepository,
        private val objectMapper: ObjectMapper
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println(">>> Seeder is running")

        try {
            val inputStream = javaClass.getResourceAsStream("/dev-data/waste.json")
            if (inputStream == null) {
                println("❌ waste.json not found!")
                return
            }

            val waste: List<Waste> = objectMapper.readValue(inputStream, object : TypeReference<List<Waste>>() {})
            println("✅ Read ${waste.size} waste")

            if (wasteRepository.count() == 0L) {
                wasteRepository.saveAll(waste)
                println("✅ Seeded waste")
            } else {
                println("ℹ️ Skipping seeding, already exists")
            }

        } catch (e: Exception) {
            println("❌ Exception during seeding: ${e.message}")
            e.printStackTrace()
        }
    }
}