package backend.unit.manager

import backend.manager.UserManager
import backend.repository.UserRepository
import backend.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class) // Aktiverer Mockito-integrasjon med JUnit 5
class UserManagerTest {

    @Mock
    private lateinit var userRepository: UserRepository // Mocket repository for å unngå databaseskall

    @InjectMocks
    private lateinit var userManager: UserManager // Oppretter UserManager og injiserer den mockede UserRepository

    @Test
    fun `createUser should generate an ID and save user`(){
        // Arrange
        val email = "test@example.com"
        val password = "example123"
        val userCaptor = ArgumentCaptor.forClass(User::class.java) // Fanger opp brukerobjektet som blir lagret

        // Act
        userManager.createUser(email, password)

        // Assert
        verify(userRepository).save(userCaptor.capture()) // Sjekker at save() ble kalt
        val savedUser = userCaptor.value // Henter brukeren som ble sendt til save()
        assertEquals(email, savedUser.email) // Sjekker at e-posten er riktig
        assertEquals(password, savedUser.password) // Sjekker at passordet er riktig
        assertNotNull(savedUser.id) // Sjekker at ID faktisk er generert
    }

    @Test
    fun `createUser should throw exception if save fails`() {
        // Arrange
        val email = "error@example.com"
        val password = "password123"
        `when`(userRepository.save(any())).thenThrow(RuntimeException("Database error")) // Simulerer en feil ved lagring

        // Act & Assert
        val exception = assertThrows<RuntimeException> {
            userManager.createUser(email, password)
        }
        assertEquals("Database error", exception.message) // Sjekker at riktig feilmelding kastes
    }

    @Test
    fun `getUserById should return user if found`() {
        // Arrange
        val userId = 1L
        val expectedUser = User(id = userId, email = "found@example.com", password = "password")
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser)) // Simulerer at brukeren finnes

        // Act
        val result = userManager.getUserById(userId)

        // Assert
        assertNotNull(result) // Skal ikke være null
        assertEquals(expectedUser, result) // Skal returnere riktig bruker
        verify(userRepository).findById(userId) // Sjekker at repository-metoden ble kalt
    }

    @Test
    fun `getUserById should return null if user not found`() {
        // Arrange
        val userId = 2L
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty()) // Simulerer at brukeren ikke finnes

        // Act
        val result = userManager.getUserById(userId)

        // Assert
        assertNull(result) // Skal returnere null
        verify(userRepository).findById(userId) // Sjekker at repository-metoden ble kalt
    }

    @Test
    fun `updateUser should update existing user and return updated user`() {
        // Arrange
        val userId = 1L
        val existingUser = User(id = userId, email = "old@example.com", password = "oldPassword")
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(existingUser)) // Bruker finnes
        `when`(userRepository.save(any())).thenAnswer { it.arguments[0] } // Returnerer brukeren den mottar

        // Act
        val result = userManager.updateUser(userId, updatedUser)

        // Assert
        assertEquals(updatedUser.email, result.email) // Sjekker at e-posten er oppdatert
        assertEquals(updatedUser.password, result.password) // Sjekker at passordet er oppdatert
        assertEquals(userId, result.id) // ID skal være den samme
        verify(userRepository).findById(userId) // Sjekker at brukeren ble hentet
        verify(userRepository).save(updatedUser) // Sjekker at oppdateringen ble lagret
    }

    @Test
    fun `updateUser should throw exception if user does not exist`() {
        // Arrange
        val userId = 2L
        val updatedUser = User(id = userId, email = "new@example.com", password = "newPassword")
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty()) // Bruker finnes ikke

        // Act & Assert
        val exception = assertThrows<NoSuchElementException> {
            userManager.updateUser(userId, updatedUser)
        }
        assertEquals("User not found", exception.message) // Sjekker riktig feilmelding
        verify(userRepository).findById(userId) // Sjekker at repository-metoden ble kalt
        verify(userRepository, never()).save(any()) // Sjekker at save() IKKE ble kalt
    }

    @Test
    fun `deleteUser should remove user if found`() {
        // Arrange
        val userId = 1L
        `when`(userRepository.existsById(userId)).thenReturn(true) // Simulerer at brukeren finnes

        // Act
        userManager.deleteUser(userId)

        // Assert
        verify(userRepository).existsById(userId) // Sjekker at repository sjekker om brukeren finnes
        verify(userRepository).deleteById(userId) // Sjekker at brukeren blir slettet
    }

    @Test
    fun `deleteUser should throw exception if user does not exist`() {
        // Arrange
        val userId = 2L
        `when`(userRepository.existsById(userId)).thenReturn(false) // Simulerer at brukeren ikke finnes

        // Act
        val exception = assertThrows<NoSuchElementException> {
            userManager.deleteUser(userId)
        }
        assertEquals("User not found", exception.message) // Sjekker riktig feilmelding
        verify(userRepository).existsById(userId) // Sjekker at repository sjekker om brukeren finnes
        verify(userRepository, never()).deleteById(any()) // Sjekker at deleteById IKKE ble kalt.
    }
}