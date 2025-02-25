package backend.manager

import backend.model.User
import backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserManager (private val userRepository: UserRepository) {

    fun createUser(email: String, password: String): User {
        val user = User(email = email, password = password)
        return userRepository.save(user)
    }

    fun getUserById(id: UUID): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun updateUser(id: UUID, updatedUser: User) : User {
        val existingUser = userRepository.findById(id).orElseThrow { RuntimeException("User not found") }
        val userToUpdate = existingUser.copy(email = updatedUser.email, password = updatedUser.password)

        return userRepository.save(userToUpdate)
    }
    
    fun deleteUser(id: UUID) {
        if (!userRepository.existsById(id)) {
            throw RuntimeException("User not found")
        }
        userRepository.deleteById(id)
    }
}