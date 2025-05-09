package backend.repository

import backend.model.Waste
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WasteRepository : JpaRepository<Waste, UUID>{
    fun findAllByTypeIn(types: List<String>): List<Waste>
}


