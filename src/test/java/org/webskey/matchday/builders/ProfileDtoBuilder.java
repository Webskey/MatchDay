package org.webskey.matchday.builders;

import org.webskey.matchday.dto.ProfileDto;

public class ProfileDtoBuilder {
	
	private static ProfileDto profileDto;
	
	public static void build() {
		profileDto = new ProfileDto();
		profileDto.setId(2);
		profileDto.setUsername("user");
		profileDto.setEmail("yerbashop.project@gmail.com");
		profileDto.setFirstname("Firstname");
		profileDto.setLastname("Lastname");
		profileDto.setPhoneNumber("123-456-789");
		profileDto.setDateOfBirth("15-02-1999");
		profileDto.setCountry("Poland");
		profileDto.setCity("Warsaw");
		profileDto.setAdress("Aleje Jerozolimskie 15/8");
		profileDto.setZipcode("11-222");
	}
	
	public static ProfileDto get() {
		build();
		return profileDto;
	}
}
