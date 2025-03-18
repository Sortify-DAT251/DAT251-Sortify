package backend.manager

import backend.model.User
import backend.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.NoSuchElementException

@Service
class UserManager (private val userRepository: UserRepository) {

    // Hashes and salts the password, for safe managing
    private val passwordEncoder = BCryptPasswordEncoder()

    fun createUser(username: String, email: String, password: String): User {

        // Salt and hash password before storing it
        val safePassword = passwordEncoder.encode(password)

        val user = User(username = username, email = email, password = safePassword)
        return userRepository.save(user)
    }

    fun getUserById(id: UUID): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun updateUser(id: UUID, updatedUser: User) : User {
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Salt and hash password before updating it
        val safePassword = passwordEncoder.encode(updatedUser.password)

        val userToUpdate = existingUser.copy(username = updatedUser.username, email = updatedUser.email, password = safePassword)

        return userRepository.save(userToUpdate)
    }

    fun deleteUser(id: UUID) {
        if (!userRepository.existsById(id)) {
            throw NoSuchElementException("User not found")
        }

        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User with ID: $id not found") }

        // Remove the user from all other users friendsLists, before deletion.
        user.friends.forEach { friend ->
            friend.friends.remove(user)
            userRepository.save(friend)
        }

        userRepository.deleteById(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun updateUserLocation(id: UUID, latitude: Double, longitude: Double): User {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        user.latitude = latitude
        user.longitude = longitude
        return userRepository.save(user)
    }

    fun getUserLocation(id: UUID): Pair<Double?, Double?> {
        val user = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        return Pair(user.latitude, user.longitude)
    }

    // Adds friend to users friendsList, and adds user to friends friendsList.
    // Returns the user object.
    fun addFriend(userId: UUID, friendId: UUID) {
        if(!userRepository.existsById(userId)) {
            throw NoSuchElementException("User not found")
        }
        if(!userRepository.existsById(friendId)) {
            throw NoSuchElementException("Friend not found")
        }

        val friend = userRepository.findById(friendId).get()
        val user = userRepository.findById(userId).get()

        user.friends.add(friend)
        friend.friends.add(user)

        userRepository.save(friend)
        userRepository.save(user)
    }

    // Removes friend from users friendsList and vice versa.
    // Returns the user object.
    fun removeFriend(userId: UUID, friendId: UUID) {
        if(!userRepository.existsById(userId)) {
            throw NoSuchElementException("User not found")
        }
        if(!userRepository.existsById(friendId)) {
            throw NoSuchElementException("Friend not found")
        }

        val friend = userRepository.findById(friendId).get()
        val user = userRepository.findById(userId).get()

        user.friends.remove(friend)
        friend.friends.remove(user)

        userRepository.save(friend)
        userRepository.save(user)
    }
}