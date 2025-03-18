package backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HomeController {

    @GetMapping("/message")
    fun getMessage(): Map<String, String> {
        return mapOf("message" to "Hello World from Sortify!")
    }
}