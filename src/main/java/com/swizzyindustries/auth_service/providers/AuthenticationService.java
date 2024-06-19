package com.swizzyindustries.auth_service.providers;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swizzyindustries.auth_service.models.CodeLord;
import com.swizzyindustries.auth_service.models.CodeLordRepository;
import com.swizzyindustries.auth_service.models.dto.RegistrationRequest;
import com.swizzyindustries.auth_service.models.role.RoleRepository;
import com.swizzyindustries.auth_service.models.token.Token;
import com.swizzyindustries.auth_service.models.token.TokenRepository;
import com.swizzyindustries.auth_service.providers.email.EmailTemplateName;

import jakarta.mail.MessagingException;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final RoleRepository roleRepository;
	private final CodeLordRepository codeLordRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final EmailService emailService;
	@Value("${application.mailing.activationUrl}")
	private String activationUrl;
	
	public void registerCodeLord(RegistrationRequest request) throws MessagingException {
		var codeLordRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new IllegalStateException("Role USER was not initialised"));
		var codeLord = CodeLord.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.email(request.getEmail())
				.codeLordName(request.getCodeLordName())
				.dateOfBirth(request.getDateOfBirth())
				.countryCode(request.getCountryCode())
				.phoneNumber(request.getPhoneNumber())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(false)
				.roles(List.of(codeLordRole))
				.build();
		codeLordRepository.save(codeLord);
		sendValidationEmail(codeLord);
				
	}

	private void sendValidationEmail(CodeLord codeLord) throws MessagingException {

		var newToken = generateAndSaveActivationToken(codeLord);
		
		emailService.sendMail(
				codeLord.getEmail(),
				codeLord.getFullName(),
				EmailTemplateName.ACTIVATE_ACCOUNT,
				activationUrl,
				newToken,
				"Account Activation"
		);
	}

	private String generateAndSaveActivationToken(CodeLord codeLord) {
		String generatedToken = generatedActivationCode(10);
		var token = Token.builder()
				.token(generatedToken)
				.createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(15))
				.codeLord(codeLord)
				.build();
		tokenRepository.save(token);
		return null;
	}

	private String generatedActivationCode(int length) {
		String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&_+|:.,=-?";
		StringBuilder codeBuilder = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();
		for(int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(characters.length());
			codeBuilder.append(characters.charAt(randomIndex));
		}
		return codeBuilder.toString();
	}

}
