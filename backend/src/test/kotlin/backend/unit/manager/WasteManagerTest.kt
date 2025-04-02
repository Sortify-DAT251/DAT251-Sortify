package backend.unit.manager

import backend.manager.WasteManager
import backend.model.Waste
import backend.repository.WasteRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull
import java.util.*

@ExtendWith(MockitoExtension::class)
class WasteManagerTest {

    @Mock
    private lateinit var wasteRepository: WasteRepository

    @InjectMocks
    private lateinit var wasteManager: WasteManager

    @Test
    fun `createWaste should create an id and save the waste`() {


        val name = "Plast"
        val type = "Tekstil"
        val info = "Tekstiler er..."

        `when`(wasteRepository.save(any())).thenAnswer { invocation -> invocation.arguments[0] as Waste }

        val createdWaste = wasteManager.createWaste(name, type, info)
        val wasteCaptor = ArgumentCaptor.forClass(Waste::class.java)
        verify(wasteRepository).save(wasteCaptor.capture())
        val savedWaste = wasteCaptor.value


        assertEquals(name, savedWaste.name)
        assertEquals(type, savedWaste.type)
        assertEquals(info, savedWaste.info)
        assertNull(savedWaste.id, "ID should not be generated before saving")
        assertEquals(savedWaste, createdWaste)
    }

    @Test
    fun `createWaste should throw exception if save fails`() {

        val name = "Plast"
        val type = "Tekstil"
        val info = "Tekstiler er..."

        `when`(wasteRepository.save(any())).thenThrow(RuntimeException("Database error"))

        val exception = org.junit.jupiter.api.assertThrows<RuntimeException> {
            wasteManager.createWaste(name, type, info)
        }
        Assertions.assertEquals("Database error", exception.message)
    }

    @Test
    fun `getWasteById should return waste if found`() {
        val wasteId = UUID.randomUUID()
        val expectedWaste = Waste(id = wasteId, name = "Plast", type = "Papir", info ="Papir er...")
        `when`(wasteRepository.findById(wasteId)).thenReturn(Optional.of(expectedWaste))

        val result = wasteManager.getWasteById(wasteId)

        assertNotNull(result)
        assertEquals(expectedWaste, result)
        verify(wasteRepository).findById(wasteId)
    }

    @Test
    fun `getWasteById should return null if waste is not found`() {
        val wasteId = UUID.randomUUID()
        `when`(wasteRepository.findById(wasteId)).thenReturn(Optional.empty())

        val result = wasteManager.getWasteById(wasteId)

        assertNull(result)
        verify(wasteRepository).findById(wasteId)
    }

    @Test
    fun `updateWaste should update existing waste and return updated waste`() {
        val wasteId = UUID.randomUUID()
        val existingWaste = Waste(id = wasteId, name = "Plast", type = "oldType", info = "old info...")
        val updatedWaste = Waste(id = wasteId, name = "Plast", type = "newType", info = "new info...")

        `when`(wasteRepository.findById(wasteId)).thenReturn(Optional.of(existingWaste))
        `when`(wasteRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = wasteManager.updateWaste(wasteId, updatedWaste)


        assertEquals(updatedWaste.name, result.name)
        assertEquals(updatedWaste.type, result.type)
        assertEquals(updatedWaste.info, result.info)
        assertEquals(wasteId, result.id)
        verify(wasteRepository).findById(wasteId)
    }

    @Test
    fun `updateWaste should throw exception if waste does not exist`() {
        val wasteId = UUID.randomUUID()
        val updatedWaste = Waste(id = wasteId, name = "Plast", type = "Plastic", info = "Recyclable plastic waste")
        `when`(wasteRepository.findById(wasteId)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            wasteManager.updateWaste(wasteId, updatedWaste)
        }

        assertEquals("Waste not found", exception.message)
        verify(wasteRepository).findById(wasteId)
        verify(wasteRepository, never()).save(any())
    }

    @Test
    fun `deleteWaste should remove waste if found`() {
        val wasteId = UUID.randomUUID()
        val waste = Waste(id = wasteId, name = "Plast", type = "Glass", info = "Glass bottles and jars")
        `when`(wasteRepository.existsById(wasteId)).thenReturn(true)
        `when`(wasteRepository.findById(wasteId)).thenReturn(Optional.of(waste))

        wasteManager.deleteWaste(wasteId)

        verify(wasteRepository).existsById(wasteId)
        verify(wasteRepository).deleteById(wasteId)
    }

    @Test
    fun `deleteWaste should throw exception if waste does not exist`() {
        val wasteId = UUID.randomUUID()
        `when`(wasteRepository.existsById(wasteId)).thenReturn(false)

        val exception = assertThrows<NoSuchElementException> {
            wasteManager.deleteWaste(wasteId)
        }

        assertEquals("Waste not found", exception.message)
        verify(wasteRepository).existsById(wasteId)
        verify(wasteRepository, never()).deleteById(any())
    }

}