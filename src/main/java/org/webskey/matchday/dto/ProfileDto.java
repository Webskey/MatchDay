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
	@Email
	@Size(min=5, max=50)
	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private String phoneNumber;
	
	private String dateOfBirth;
	
	private String country;	
	
	private String city;
	
	private String adress;
	
	private String zipcode;	
}
