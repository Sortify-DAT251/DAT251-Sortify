package backend.unit.repository

import backend.config.PostGresTestConfig
import backend.model.Waste
import backend.repository.WasteRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.event.TransactionalEventListener
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Import(PostGresTestConfig::class)  // Import PostgreSQL TestContainer Config
@ExtendWith(SpringExtension::class)
class WasteRepositoryTest {

    @Autowired
    private lateinit var wasteRepository: WasteRepository

    @Test
    @Transactional
    fun `save should persist waste and generate ID`() {
        val waste = Waste(name = "Plast", type = "Plast", info = "Plast er...")
        val savedWaste = wasteRepository.save(waste)

        assertNotNull(savedWaste.id)
        assertEquals(waste.type, savedWaste.type)
        assertEquals(waste.info, savedWaste.info)
    }

    @Test
    @Transactional
    fun `findById should return waste if it exists`() {
        val waste = wasteRepository.save(Waste(name = "Plast", type = "Plast", info = "Plast er..."))
        val foundWaste = wasteRepository.findById(waste.id!!)

        assertTrue(foundWaste.isPresent)
        assertEquals(waste.type, foundWaste.get().type)
        assertEquals(waste.info, foundWaste.get().info)
    }

    @Test
    @Transactional
    fun `findById should return empty if waste does not exist`() {
        val nonExistentId = UUID.randomUUID()
        val foundWaste = wasteRepository.findById(nonExistentId)

        assertTrue(foundWaste.isEmpty)
    }

    @Test
    @Transactional
    fun `deleteById should remove waste`() {
        val waste = wasteRepository.save(Waste(name = "Plast",type = "Plast", info = "Plast er..."))
        assertTrue(wasteRepository.findById(waste.id!!).isPresent)

        wasteRepository.deleteById(waste.id!!)

        assertFalse(wasteRepository.findById(waste.id!!).isPresent)
    }
}