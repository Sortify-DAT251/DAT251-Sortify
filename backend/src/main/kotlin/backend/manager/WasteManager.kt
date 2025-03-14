package backend.manager

import backend.model.Waste
import backend.repository.WasteRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class WasteManager(private val wasteRepository: WasteRepository) {

    fun createWaste(type: String, info: String): Waste {
        val waste = Waste(type = type, info = info)
        return wasteRepository.save(waste)
    }

    fun getWasteById(id: UUID): Waste? {
        return wasteRepository.findById(id).orElse(null)
    }

    fun updateWaste(id: UUID, updatedWaste: Waste): Waste {
        val existingWaste = wasteRepository.findById(id).orElseThrow { NoSuchElementException("Waste not found") }
        val wasteToUpdate = existingWaste.copy(type = updatedWaste.type, info = updatedWaste.info)

        return wasteRepository.save(wasteToUpdate)
    }

    fun deleteWaste(id: UUID) {
        if (!wasteRepository.existsById(id)) {
            throw NoSuchElementException("Waste not found")
        }

        val waste = wasteRepository.findById(id).orElseThrow { NoSuchElementException("Waste with ID: $id not found") }

        wasteRepository.deleteById(id)
    }

    fun getAllWaste(): List<Waste> {
        return wasteRepository.findAll()
    }
}