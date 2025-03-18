// package backend.security

// import io.jsonwebtoken.Jwts
// import org.springframework.security.core.context.SecurityContextHolder
// import org.springframework.web.filter.OncePerRequestFilter
// import javax.servlet.Filter
// import javax.servlet.FilterChain
// import javax.servlet.ServletException
// import javax.servlet.ServletRequest
// import javax.servlet.ServletResponse
// import javax.servlet.http.HttpServletRequest
// import java.io.IOException

// class JwtAuthenticationFilter : OncePerRequestFilter() {

//     override fun doFilterInternal(
//         request: HttpServletRequest, response: javax.servlet.http.HttpServletResponse,
//         chain: FilterChain
//     ) {
//         val token = request.getHeader("Authorization")?.removePrefix("Bearer ")

//         if (token != null && JwtUtil.validateToken(token)) {
//             val username = JwtUtil.extractUsername(token)
//             val authentication = UsernamePasswordAuthenticationToken(username, null, listOf())
//             SecurityContextHolder.getContext().authentication = authentication
//         }

//         chain.doFilter(request, response)
//     }
// }
