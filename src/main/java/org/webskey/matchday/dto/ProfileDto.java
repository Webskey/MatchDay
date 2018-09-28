package org.webskey.matchday.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileDto {
	
	private int id;

	@NotNull
	@Size(min=3, max=10, message="Username should have 3-10 characters")
	private String username;

	@NotNull
	@Size(min=5, max=15, message="Password should have 5-15 characters")
	private String password;
	
	@NotNull
	@Size(min=5, max=15, message="Password should have 5-15 characters")
	private String oldPassword;
	
	@NotNull
	@Email
	@Size(min=5, max=50)
	private String email;
}
