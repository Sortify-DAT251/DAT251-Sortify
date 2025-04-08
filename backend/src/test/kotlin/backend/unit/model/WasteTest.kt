package backend.unit.model

import backend.model.Waste
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.springframework.test.context.ActiveProfiles
@ActiveProfiles("test")
class WasteTest {

    @Test
    fun `should generate an id for waste type automatically`() {
        val waste = Waste(id = UUID.randomUUID(), name = "Plastpose", type = "plast", info = "Plast er...")
        assertNotNull(waste.id ,"Waste_id should no be null")
        assertTrue(waste.id is UUID, "waste_id should be of type UUID")
    }

    @Test
    fun `waste object should always have a type`(){
        val waste = Waste(id = UUID.randomUUID(), name = "T-skjorte", type = "tekstil", info = "Plast er...")
        assertNotNull(waste.type ,"Waste_type should no be null")
    }

}