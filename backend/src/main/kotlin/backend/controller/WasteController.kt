package backend.controller

import backend.manager.WasteManager
import backend.model.User
import backend.model.Waste
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/waste")
@Validated
class WasteController(private val wasteManager: WasteManager) {

    @PostMapping
    fun createWaste(@RequestBody @Valid request: WasteRequest): ResponseEntity<Any> {
        return try {
            val waste = wasteManager.createWaste(request.name, request.type, request.info)
            ResponseEntity.status(HttpStatus.CREATED).body(waste)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Username or email is already in use."))
        }
    }

    @GetMapping("/{id}")
    fun getWaste(@PathVariable id: UUID): ResponseEntity<Waste> {
        val waste = wasteManager.getWasteById(id)
        return if (waste != null) ResponseEntity.ok(waste)
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}")
    fun updateWaste(@PathVariable id: UUID, @RequestBody @Valid request: WasteRequest): ResponseEntity<Waste> {
        return try {
            val updatedWaste = Waste(name = request.name, type = request.type, info = request.info)
            val waste = wasteManager.updateWaste(id, updatedWaste)
            if (waste != null) {
                ResponseEntity.ok(waste)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteWaste(@PathVariable id: UUID): ResponseEntity<Void> {
        return try {
            wasteManager.deleteWaste(id)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    fun getAllWaste(): ResponseEntity<List<Waste>> {
        val waste = wasteManager.getAllWaste()
        return ResponseEntity.ok(waste)
    }
}

data class WasteRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val type: String,

    @field:NotBlank
    val info: String
)