package backend.unit.controller

import backend.config.TestSecurityConfig
import backend.controller.UserController
import backend.manager.UserManager
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class)
class WasteControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper = ObjectMapper()

    @MockBean
    private lateinit var userManager: UserManager
}