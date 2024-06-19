/**
 * 
 */
package com.swizzyindustries.auth_service.models.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
@Builder
public class RegistrationRequest {
	@NotEmpty(message = "First name is mandatory.")
	@NotBlank(message = "First name cannot be empty.")
	private String firstname;
	
	@NotEmpty(message = "Last name is mandatory.")
	@NotBlank(message = "First name cannot be empty.")
    private String lastname;
	
	@NotEmpty(message = "CodeLord username is mandatory.")
	@NotBlank(message = "CodeLord username cannot be empty.")
    private String codeLordName;
	
	@Email(message = "E-mail is not well formatted --> ***@***.***")
	@NotEmpty(message = "An E-mail is mandatory.")
	@NotBlank(message = "E-mail can not be empty.")
    private String email;
	
	@Min(value = 0, message = "Phone number length should be >=9.")
	@Max(value = 999999999, message = "Phone number length should be <= 11.")
    private Integer phoneNumber;
	
	@Min(value = 1, message = "Phone number length should be >=1.")
	@Max(value = 999, message = "Phone number length should be <= 3.")
    private Integer countryCode;
    
	@NotNull
	@Past
    private LocalDate dateOfBirth;
    
    @NotEmpty(message = "A password is mandatory.")
	@NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message ="Password should have 8 or more characters.")
    private String password;
}
