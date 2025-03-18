package backend.repository

import backend.model.Locations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LocationsRepository : JpaRepository<Locations, UUID>