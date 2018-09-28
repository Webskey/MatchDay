package org.webskey.matchday.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PasswordDto {
	
	@NotNull
	@Size(min=5, max=15, message="Password should have 5-15 characters")
	private String password;
	
	@NotNull
	@Size(min=5, max=15, message="Password should have 5-15 characters")
	private String oldPassword;
	
	@NotNull
	@Size(min=5, max=15, message="Password should have 5-15 characters")
	private String confirmPassword;
	
}
