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

    fun createUser(username: String, email: String, password: String, firstName: String? = null, lastName: String? = null): User {
        if (userRepository.existsByUsername(username)) {
            throw IllegalArgumentException("Username already exists")
        }
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email already exists")
        }
        // Salt and hash password before storing it
        val safePassword = passwordEncoder.encode(password)

        val user = User(
            username = username,
            email = email,
            password = safePassword,
            firstName = firstName,
            lastName = lastName)
        return userRepository.save(user)
    }

    fun getUserById(id: UUID): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun updateUser(id: UUID, updatedUser: User) : User {
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Salt and hash password before updating it
        val safePassword = passwordEncoder.encode(updatedUser.password)

        val userToUpdate = existingUser.copy(
            username = updatedUser.username,
            email = updatedUser.email,
            password = safePassword,
            firstName = updatedUser.firstName,
            lastName = updatedUser.lastName)

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

    fun loginUser(identifier: String, password: String): User {
        val user = userRepository.findByUsername(identifier) ?: userRepository.findByEmail(identifier)
            ?: throw NoSuchElementException("User not found")

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Wrong password")
        }

        return user
    }
}