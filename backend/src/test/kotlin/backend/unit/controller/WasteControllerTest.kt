package backend.unit.controller

import backend.config.TestSecurityConfig
import backend.controller.WasteController
import backend.manager.WasteManager
import backend.model.Waste
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@WebMvcTest(WasteController::class)
@Import(TestSecurityConfig::class)
class WasteControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @MockBean
    private lateinit var wasteManager: WasteManager

    @Test
    fun `should create waste successfully`() {
        val wasteId = UUID.randomUUID()
        val waste = Waste(id = wasteId, name = "Plastpose", type = "Plast", info = "Plast er...")
        val requestBody = objectMapper.writeValueAsString(mapOf("name" to "Plastpose", "type" to "Plast", "info" to "Plast er..."))

        whenever(wasteManager.createWaste(any(), any(), any())).thenReturn(waste)

        mockMvc.perform(
            post("/waste")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(wasteId.toString()))
            .andExpect(jsonPath("$.name").value("Plastpose"))
            .andExpect(jsonPath("$.type").value("Plast"))
            .andExpect(jsonPath("$.info").value("Plast er..."))
    }

    @Test
    fun `should return 400 Bad Request for invalid waste creation`() {
        val requestBody = objectMapper.writeValueAsString(mapOf("name" to "", "type" to "", "info" to ""))

        mockMvc.perform(post("/waste")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should retrieve waste by ID`() {
        val wasteId = UUID.randomUUID()
        val waste = Waste(id = wasteId, name = "T-skjorte", type = "Tekstil", info = "Tekstiler er...")

        `when`(wasteManager.getWasteById(wasteId)).thenReturn(waste)

        mockMvc.perform(get("/waste/$wasteId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(wasteId.toString()))
            .andExpect(jsonPath("$.name").value("T-skjorte"))
            .andExpect(jsonPath("$.type").value("Tekstil"))
            .andExpect(jsonPath("$.info").value("Tekstiler er..."))
    }

    @Test
    fun `should return 404 when waste doesnt exist`() {
        val wasteId = UUID.randomUUID()

        `when`(wasteManager.getWasteById(wasteId)).thenReturn(null)

        mockMvc.perform(get("/waste/$wasteId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete waste successfully`() {
        val wasteId = UUID.randomUUID()

        doNothing().`when`(wasteManager).deleteWaste(wasteId)
        mockMvc.perform(delete("/waste/$wasteId"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when deleting non-existent waste`() {
        val wasteId = UUID.randomUUID()

        doThrow(NoSuchElementException("Waste not found")).`when`(wasteManager).deleteWaste(wasteId)

        mockMvc.perform(delete("/waste/$wasteId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllWaste should return HTTP 200 with waste`() {
        val waste1 = Waste(
            id = UUID.randomUUID(),
            name = "Plastpose",
            type = "Plast",
            info = "Plast er..."
        )

        val waste2 = Waste(
            id = UUID.randomUUID(),
            name = "T-skjorte",
            type = "Tekstil",
            info = "Tekstiler er..."
        )

        `when`(wasteManager.getAllWaste()).thenReturn(listOf(waste1, waste2))

        mockMvc.get("/waste")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].id") { value(waste1.id.toString()) }
                jsonPath("$[0].name") {value("Plastpose")}
                jsonPath("$[0].type") { value("Plast") }
                jsonPath("$[0].info") { value("Plast er...") }
                jsonPath("$[1].id") { value(waste2.id.toString()) }
                jsonPath("$[1].name") {value("T-skjorte")}
                jsonPath("$[1].type") { value("Tekstil") }
                jsonPath("$[1].info") { value("Tekstiler er...") }
            }
    }
}