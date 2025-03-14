// package backend

// import backend.model.User
// import backend.repository.UserRepository
// import backend.config.PostGresTestConfig 
// import org.junit.jupiter.api.Assertions.*
// import org.junit.jupiter.api.Test
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.transaction.annotation.Transactional
// import java.util.*

// @SpringBootTest
// @Import(PostGresTestConfig::class)
// class DatabaseConfigTest {

//     @Autowired
//     lateinit var userRepository: UserRepository

//     @Test
//     @Transactional
//     fun saveUserToDatabaseTest() {
//         // Create a new user
//         val user = User(username = "testuser", email = "testuser@example.com", password = "password123")

//         // Save the user to the database
//         val savedUser = userRepository.save(user)

//         // Verify the user was saved and can be retrieved
//         assertNotNull(savedUser)
//         assertEquals(user.username, savedUser.username)
//         assertEquals(user.email, savedUser.email)

//         // Try to fetch the user again from the database
//         val retrievedUser = userRepository.findById(savedUser.id!!)
//         assertTrue(retrievedUser.isPresent)
//         assertEquals(savedUser.username, retrievedUser.get().username)
//         assertEquals(savedUser.email, retrievedUser.get().email)
//     }
// }
