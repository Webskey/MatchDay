package org.webskey.matchday.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UsersDto {

	private int id;

	@NotNull
	@Size(min=3, max=10, message="3-10 characters")
	private String username;

	@NotNull
	@Size(min=5, max=15, message="5-15 characters")
	private String password;
	
	@NotNull
	@Email(message="wrong email")
	private String email;
}
