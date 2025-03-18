// package backend.controller

// import backend.manager.UserManager
// import backend.security.JwtUtil
// import backend.model.User
// import org.springframework.http.HttpStatus
// import org.springframework.http.ResponseEntity
// import org.springframework.web.bind.annotation.*
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
// import jakarta.validation.Valid

// @RestController
// @RequestMapping("/auth")
// class AuthController(private val userManager: UserManager) {

//     private val passwordEncoder = BCryptPasswordEncoder()

//     @PostMapping("/login")
//     fun login(@RequestBody @Valid credentials: LoginRequest): ResponseEntity<Map<String, String>> {
//         val user = userManager.getUserByEmail(credentials.email)

//         return if (user != null && passwordEncoder.matches(credentials.password, user.password)) {
//             // Generate JWT token
//             val token = JwtUtil.generateToken(user.username)

//             ResponseEntity.ok(mapOf("token" to token))
//         } else {
//             ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
//         }
//     }
// }

// data class LoginRequest(
//     val email: String,
//     val password: String
// )
