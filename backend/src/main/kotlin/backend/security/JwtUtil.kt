// package backend.security

// import io.jsonwebtoken.Jwts
// import io.jsonwebtoken.SignatureAlgorithm
// import java.util.Date

// object JwtUtil {
//     private const val SECRET_KEY = "yourSecretKey" // Use a better secret key for production!

//     // Create a JWT token for a user
//     fun generateToken(username: String): String {
//         return Jwts.builder()
//             .setSubject(username)
//             .setIssuedAt(Date())
//             .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration time
//             .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//             .compact()
//     }

//     // Validate the JWT token
//     fun validateToken(token: String): Boolean {
//         try {
//             Jwts.parser()
//                 .setSigningKey(SECRET_KEY)
//                 .parseClaimsJws(token)
//             return true
//         } catch (e: Exception) {
//             return false
//         }
//     }

//     // Get username from JWT token
//     fun extractUsername(token: String): String {
//         return Jwts.parser()
//             .setSigningKey(SECRET_KEY)
//             .parseClaimsJws(token)
//             .body.subject
//     }
// }
