/**
 * 
 */
package com.swizzyindustries.auth_service.configs;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class JwtService {
	
	@Value("${application.security.expiration}")
	private long jwtExpiration;
	@Value("${application.security.secret-key}")
	private String secretKey;

	public String extractCodeLordEmailFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts
					.parserBuilder()
					.setSigningKey(getSignInKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}

	public String generateToken(UserDetails codeLordDetails) {
		return generateToken(new HashMap<>(), codeLordDetails);
	}

	private String generateToken(Map<String, Object> claims, UserDetails codeLordDetails) {
		return buildToken(claims, codeLordDetails, jwtExpiration);
	}

	private String buildToken(
			Map<String, Object> extraClaims, 
			UserDetails codeLordDetails, 
			long jwtExpiration) {
		var authorities = codeLordDetails.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		return Jwts
					.builder()
					.setClaims(extraClaims)
					.setSubject(codeLordDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
					.claim("authorities", authorities)
					.signWith(getSignInKey())
					.compact();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public boolean isTokenValid(String token, UserDetails codeLordDetails) {
		final String username = extractCodeLordEmailFromToken(token);
		return (username.equals(codeLordDetails.getUsername()) && !isTokenExpired(token));
	}


	private boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration);
	}
}
