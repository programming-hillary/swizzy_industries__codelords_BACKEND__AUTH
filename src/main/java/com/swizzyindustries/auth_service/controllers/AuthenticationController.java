/**
 * 
 */
package com.swizzyindustries.auth_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.swizzyindustries.auth_service.models.dto.RegistrationRequest;
import com.swizzyindustries.auth_service.providers.AuthenticationService;

//import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
//@Tag(name = "authentication")
public class AuthenticationController {
	
	private final AuthenticationService authService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> register(
						@RequestBody @Valid RegistrationRequest request
			) throws MessagingException {
		authService.registerCodeLord(request);
		return ResponseEntity.accepted().build();
	}

}
