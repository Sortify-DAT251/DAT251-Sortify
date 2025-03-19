package backend.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.hamcrest.Matchers.containsString

@SpringBootTest
@AutoConfigureMockMvc
// Update your test classes to use the H2 database configuration instead of using separate applications-test.properties file
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.jpa.show-sql=true"
])
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun homePageTest() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpect(model().attribute("message", "Hello World"))
            .andExpect(content().string(containsString("Hello World")))
    }
}