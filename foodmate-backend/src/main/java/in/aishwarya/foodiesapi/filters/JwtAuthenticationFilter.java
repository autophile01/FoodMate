package in.aishwarya.foodiesapi.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import in.aishwarya.foodiesapi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		    final String authHeader = request.getHeader("Authorization");
		    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {

		        String token = authHeader.substring(7);
		        String email = jwtUtil.extractUsername(token);

		        if (email != null &&
		            SecurityContextHolder.getContext().getAuthentication() == null) {

		            UserDetails userDetails =
		                    userDetailsService.loadUserByUsername(email);
		            
//LATEST CHANGES TODAY
//		            if(jwtUtil.validateToken(token, userDetails)) {
//		            	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//		            			userDetails, null, userDetails.getAuthorities());
//		            	authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		            	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//		            }
		            
//NEW CODE ADDED
		            if (jwtUtil.validateToken(token, userDetails)) {

		                UsernamePasswordAuthenticationToken authenticationToken =
		                        new UsernamePasswordAuthenticationToken(
		                                userDetails,
		                                null,
		                                userDetails.getAuthorities()
		                        );

		                authenticationToken.setDetails(
		                        new WebAuthenticationDetailsSource().buildDetails(request)
		                );

		                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		                System.out.println("JWT VALID");
		                System.out.println("USER: " + userDetails.getUsername());
		                System.out.println("AUTHORITIES: " + userDetails.getAuthorities());
		                System.out.println("AUTHENTICATED: " +
		                        SecurityContextHolder.getContext()
		                                .getAuthentication()
		                                .isAuthenticated());
		            }else {		        
		            	    System.out.println("JWT INVALID");
		            }
		         
		            
		        }
		    }
		    filterChain.doFilter(request, response);
	}
       
}
